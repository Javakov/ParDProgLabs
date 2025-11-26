plugins {
    id("java")
    id("application")
}

group = "org.javakov"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass.set("org.javakov.md5.Md5BruteForceApp")
}

tasks.register<JavaExec>("runGalton") {
    group = "application"
    description = "Запускает симуляцию доски Гальтона"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("org.javakov.galton.GaltonBoardApp")
    standardInput = System.`in`
}

tasks.register<JavaExec>("runKnapsack") {
    group = "application"
    description = "Запускает параллельное решение задачи о рюкзаке"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("org.javakov.knapsack.KnapsackApp")
}

tasks.register<JavaExec>("runTsp") {
    group = "application"
    description = "Запускает параллельное решение задачи коммивояжёра"
    classpath = sourceSets.main.get().runtimeClasspath
    mainClass.set("org.javakov.tsp.TspApp")
}