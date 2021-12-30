package spp.processor.logging.impl

import com.google.protobuf.Message
import org.apache.skywalking.apm.network.logging.v3.LogData
import org.apache.skywalking.oap.log.analyzer.provider.log.listener.LogAnalysisListener
import org.apache.skywalking.oap.log.analyzer.provider.log.listener.LogAnalysisListenerFactory
import org.slf4j.LoggerFactory

class LogPatternAnalysis : LogAnalysisListenerFactory {

    companion object {
        private val log = LoggerFactory.getLogger(LogPatternAnalysis::class.java)
    }

    private val sppLogAnalyzer = object : LogAnalysisListener {
        override fun build() = Unit

        override fun parse(logData: LogData.Builder, p1: Message?): LogAnalysisListener {
            log.info("Parsing log data {}", logData)
            return this
        }
    }

    override fun create() = sppLogAnalyzer
}
