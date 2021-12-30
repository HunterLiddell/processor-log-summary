package spp.processor.logging.provider

import org.apache.skywalking.oap.log.analyzer.module.LogAnalyzerModule
import org.apache.skywalking.oap.log.analyzer.provider.log.ILogAnalyzerService
import org.apache.skywalking.oap.log.analyzer.provider.log.LogAnalyzerServiceImpl
import org.apache.skywalking.oap.server.core.storage.StorageModule
import org.apache.skywalking.oap.server.library.module.ModuleConfig
import org.apache.skywalking.oap.server.library.module.ModuleDefine
import org.apache.skywalking.oap.server.library.module.ModuleProvider
import org.slf4j.LoggerFactory
import spp.processor.LogSummaryProcessor
import spp.processor.logging.impl.LogPatternAnalysis

class LoggingModule : ModuleDefine("spp-log-summary") {
    override fun services(): Array<Class<*>> = emptyArray()
}

class LoggingProcessorProvider : ModuleProvider() {

    companion object {
        private val log = LoggerFactory.getLogger(LoggingProcessorProvider::class.java)
    }

    override fun name(): String = "default"
    override fun module(): Class<out ModuleDefine> = LoggingModule::class.java
    override fun createConfigBeanIfAbsent(): ModuleConfig? = null
    override fun prepare() = Unit

    override fun start() {
        log.info("Starting LoggingProcessorProvider")
        LogSummaryProcessor.module = manager

        //drain live logs
        val logParserService = manager.find(LogAnalyzerModule.NAME)
            .provider().getService(ILogAnalyzerService::class.java) as LogAnalyzerServiceImpl
        logParserService.addListenerFactory(LogPatternAnalysis())
    }

    override fun notifyAfterCompleted() = Unit
    override fun requiredModules(): Array<String> {
        return arrayOf(
            StorageModule.NAME,
            LogAnalyzerModule.NAME
        )
    }
}
