package at.tuwien.ase.cardlabs.management

import at.tuwien.ase.cardlabs.management.controller.model.bot.TestBot
import com.opencsv.CSVParserBuilder
import com.opencsv.CSVReaderBuilder
import java.io.File
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.nio.file.Paths

class Helper {

    companion object {

        @JvmStatic
        fun requireNull(value: Any?, message: String = "Value is expected to be null") {
            if (value != null) {
                throw IllegalArgumentException(message)
            }
        }

        @JvmStatic
        fun requireNonNull(value: Any?, message: String = "Value is expected to not be null") {
            if (value == null) {
                throw IllegalArgumentException(message)
            }
        }

        @JvmStatic
        fun fetchAllTestsBots(): List<TestBot> {
            val result = mutableListOf<TestBot>()

            val file = File(ManagementApplication::class.java.classLoader.getResource("bots/bots.csv").file)

            val parser = CSVParserBuilder()
                .withSeparator(';')
                .build()
            val botMetadataList = Files.newBufferedReader(file.toPath()).use { bufferedReader ->
                CSVReaderBuilder(bufferedReader)
                    .withCSVParser(parser)
                    .build()
                    .use { csvReader ->
                        csvReader.skip(1)
                        csvReader.readAll()
                    }
            }

            for (botMetadata in botMetadataList) {
                val botCodeFileName = "bots" + botMetadata[3]
                val botCodeFileResource = ManagementApplication::class.java.classLoader.getResource(botCodeFileName)
                val botCodeFilePath = Paths.get(botCodeFileResource?.toURI())
                val botCode = Files.readAllLines(botCodeFilePath, StandardCharsets.UTF_8)
                    .joinToString(separator = "\n")
                result.add(
                    TestBot(
                        botMetadata[0].toLong(),
                        botMetadata[1],
                        botMetadata[2],
                        botCode
                    )
                )
            }

            return result
        }
    }
}
