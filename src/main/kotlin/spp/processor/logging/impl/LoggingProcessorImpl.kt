package spp.processor.logging.impl

import io.vertx.core.AsyncResult
import io.vertx.core.Future
import io.vertx.core.Handler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import org.apache.skywalking.library.elasticsearch.requests.search.Search
import org.apache.skywalking.library.elasticsearch.requests.search.aggregation.Aggregation
import org.apache.skywalking.oap.server.core.storage.StorageModule
import org.apache.skywalking.oap.server.core.storage.query.ILogQueryDAO
import org.apache.skywalking.oap.server.storage.plugin.elasticsearch.base.EsDAO
import org.elasticsearch.search.aggregations.bucket.terms.ParsedStringTerms
import org.elasticsearch.search.aggregations.bucket.terms.Terms
import org.slf4j.LoggerFactory
import spp.processor.LogSummaryProcessor
import spp.processor.logging.LoggingProcessor
import java.util.stream.Collectors

class LoggingProcessorImpl : CoroutineVerticle(), LoggingProcessor {

    companion object {
        private val log = LoggerFactory.getLogger(LoggingProcessorImpl::class.java)
    }

    private var elasticSearch: EsDAO? = null

    override suspend fun start() {
        log.info("Starting LoggingProcessorImpl")
        try {
            elasticSearch = LogSummaryProcessor.module!!.find(StorageModule.NAME).provider()
                .getService(ILogQueryDAO::class.java) as EsDAO
        } catch (ex: ClassCastException) {
            log.error("Elasticsearch storage unavailable. Disabled logging processor")
        }
    }

    override fun getPatternOccurredCounts(handler: Handler<AsyncResult<Map<String, Int>>>) {
        if (elasticSearch != null) {
            handler.handle(Future.failedFuture("Logging processor disabled"))
            return
        }

        //todo: add metric timer
        log.info("Getting pattern occurred counts")
        val size = 1000
        val aggregation = Search.builder()
            .aggregation(Aggregation.terms("content").field("content"))
            .size(size).build()
        val logPatternCounts = (elasticSearch!!.client.search("log", aggregation)
            .aggregations["content"] as ParsedStringTerms).buckets.stream()
            .collect(Collectors.toMap(Terms.Bucket::getKeyAsString) { it.docCount.toInt() })
        handler.handle(Future.succeededFuture(logPatternCounts))
    }

    override suspend fun stop() {
        log.info("Stopping LoggingProcessorImpl")
    }
}
