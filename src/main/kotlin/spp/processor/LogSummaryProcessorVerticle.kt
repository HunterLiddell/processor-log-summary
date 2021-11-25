package spp.processor

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.*
import com.fasterxml.jackson.databind.module.SimpleModule
import io.vertx.core.json.JsonObject
import io.vertx.core.json.jackson.DatabindCodec
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.await
import io.vertx.servicediscovery.Record
import io.vertx.servicediscovery.types.EventBusService
import io.vertx.serviceproxy.ServiceBinder
import kotlinx.datetime.Instant
import org.apache.skywalking.oap.server.core.analysis.metrics.DataTable
import org.slf4j.LoggerFactory
import spp.processor.common.FeedbackProcessor.Companion.INSTANCE_ID
import spp.processor.logging.LoggingProcessor
import spp.processor.logging.impl.LoggingProcessorImpl
import spp.protocol.processor.ProcessorAddress
import spp.protocol.util.KSerializers
import kotlin.system.exitProcess

class LogSummaryProcessorVerticle : CoroutineVerticle() {

    companion object {
        private val log = LoggerFactory.getLogger(LogSummaryProcessorVerticle::class.java)

        val loggingProcessor = LoggingProcessorImpl()
    }

    private var loggingRecord: Record? = null

    override suspend fun start() {
        log.info("Starting LogSummaryProcessorVerticle")
        val module = SimpleModule()
        module.addSerializer(DataTable::class.java, object : JsonSerializer<DataTable>() {
            override fun serialize(value: DataTable, gen: JsonGenerator, provider: SerializerProvider) {
                val data = mutableMapOf<String, Long>()
                value.keys().forEach { data[it] = value.get(it) }
                gen.writeStartObject()
                data.forEach {
                    gen.writeNumberField(it.key, it.value)
                }
                gen.writeEndObject()
            }
        })
        DatabindCodec.mapper().registerModule(module)

        module.addSerializer(Instant::class.java, KSerializers.KotlinInstantSerializer())
        module.addDeserializer(Instant::class.java, KSerializers.KotlinInstantDeserializer())
        DatabindCodec.mapper().registerModule(module)

        vertx.deployVerticle(loggingProcessor).await()

        ServiceBinder(vertx).setIncludeDebugInfo(true)
            .setAddress(ProcessorAddress.LOGGING_PROCESSOR.address)
            .register(LoggingProcessor::class.java, loggingProcessor)
        loggingRecord = EventBusService.createRecord(
            ProcessorAddress.LOGGING_PROCESSOR.address,
            ProcessorAddress.LOGGING_PROCESSOR.address,
            LoggingProcessor::class.java,
            JsonObject().put("INSTANCE_ID", INSTANCE_ID)
        )
        LogSummaryProcessor.discovery.publish(loggingRecord) {
            if (it.succeeded()) {
                log.info("Logging processor published")
            } else {
                log.error("Failed to publish logging processor", it.cause())
                exitProcess(-1)
            }
        }
    }

    override suspend fun stop() {
        log.info("Stopping LogSummaryProcessorVerticle")
        LogSummaryProcessor.discovery.unpublish(loggingRecord!!.registration).onComplete {
            if (it.succeeded()) {
                log.info("Logging processor unpublished")
            } else {
                log.error("Failed to unpublish logging processor", it.cause())
            }
        }.await()
    }
}
