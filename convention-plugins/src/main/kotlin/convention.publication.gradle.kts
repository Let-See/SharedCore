import gradle.kotlin.dsl.accessors._2502cef48cff830615fe1c6d6ab5e104.ext
import gradle.kotlin.dsl.accessors._2502cef48cff830615fe1c6d6ab5e104.publishing
import gradle.kotlin.dsl.accessors._2502cef48cff830615fe1c6d6ab5e104.signing
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.tasks.bundling.Jar
import org.gradle.kotlin.dsl.`maven-publish`
import org.gradle.kotlin.dsl.signing
import java.util.*

plugins {
    `maven-publish`
    signing
}
// Stub secrets to let the project sync and build without the publication values set up
ext["signing.keyId"] = null
ext["signing.password"] = null
ext["signing.secretKeyRingFile"] = null
ext["signing.key"] = null
ext["ossrhUsername"] = null
ext["ossrhPassword"] = null

val publishVersion: String by rootProject
val publishGroupId: String by rootProject

// Grabbing secrets from local.properties file or from environment variables, which could be used on CI
val secretPropsFile = project.rootProject.file("local.properties")
if (secretPropsFile.exists()) {
    secretPropsFile.reader().use {
        Properties().apply {
            load(it)
        }
    }.onEach { (name, value) ->
        ext[name.toString()] = value
    }
} else {
    ext["signing.keyId"] = System.getenv("SIGNING_KEY_ID")
    ext["signing.password"] = System.getenv("SIGNING_PASSWORD")
    ext["signing.secretKeyRingFile"] = System.getenv("SIGNING_SECRET_KEY_RING_FILE")
    ext["ossrhUsername"] = System.getenv("OSSRH_USERNAME")
    ext["ossrhPassword"] = System.getenv("OSSRH_PASSWORD")
}
println("signing.keyId")
println(project.name)
//val javadocJar by tasks.registering(Jar::class) {
//    archiveClassifier.set("javadoc")
//}

fun getExtraString(name: String) = ext[name]?.toString()

publishing {
    // Configure maven central repository
    repositories {
        maven {
            name = "sonatype"
            setUrl("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/")
            credentials {
                username = getExtraString("ossrhUsername")
                password = getExtraString("ossrhPassword")
            }
        }
    }

    // Configure all publications
    publications.withType<MavenPublication> {

        // Stub javadoc.jar artifact
        // artifact(javadocJar.get())
        groupId = publishGroupId
        version = publishVersion
        artifactId = project.name
        // Provide artifacts information requited by Maven Central
        pom {
            name.set(project.name)
            description.set("LetSee provides an easy way to provide mock data to your iOS application. The main intention of having a library like this is to have a way to mock the response of requests on runtime in an easy way to be able to test all available scenarios without the need to rerun or change the code or put in the extra effort.")
            url.set("https://github.com/let-see/SharedCore")

            licenses {
                license {
                    name.set("MIT")
                    url.set("https://opensource.org/licenses/MIT")
                }
            }
            developers {
                developer {
                    id.set("Jahanmanesh")
                    name.set("Farshad Jahanmanesh")
                    email.set("Farshad.Jahanmanesh@gmail.com")
                }
            }
            scm {
                url.set("https://github.com/Let-See/SharedCore")
            }

        }
    }
}

// Signing artifacts. Signing.* extra properties values will be used

signing {
    sign(publishing.publications)
}