package net.devslash.packer

import com.fasterxml.jackson.annotation.JsonProperty
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.jar.JarEntry
import java.util.jar.JarFile
import kotlin.streams.toList

data class LoadedClass(val packageName: String, val className: String)

class JarLibraryLoader {

  private val defaultFile =
    File("C:\\Users\\pault\\.gradle\\caches\\transforms-3\\4cdafc567c3374086b4ba84a093c758f\\transformed\\unzipped-distribution\\gradle-6.8\\subprojects\\docs\\src\\snippets\\webApplication\\customized\\groovy\\lib\\runtime-1.0.jar")
  private val source = File("~/.config/dsl")

  init {
    source.mkdirs()
  }

  private val regex = Regex("[$.]([^$.]+?)$")

  /**
   * Load the class files in the jar - We'll be looking for all classes that allow for body transforms.
   *
   * We can save this to the DB - but we do need
   */
  fun load(jarPath: File = defaultFile): Collection<LoadedClass> {
    val copied = jarPath.copyTo(source.resolve(File(jarPath.name)), true)
    val jar = JarFile(copied)

    val classNames = jar.stream().flatMap {
      if (!it.isDirectory && it.name.endsWith(".class")) {
        listOf(it).stream()
      } else {
        listOf<JarEntry?>().stream()
      }
    }
      .filter { it.name.endsWith("class") }
      .map { it.name.substring(0, it.name.length - ".class".length) }
      .map { it.replace("/", ".") }

    // Lets load them all
    val loader = URLClassLoader(arrayOf(URL("jar:file:${copied.absolutePath}!/")))

    return classNames.flatMap { names ->
      val clazz: Class<*>?
      try {
        clazz = loader.loadClass(names)


        val all: MutableSet<Class<*>> = mutableSetOf(clazz)
        all.addAll(clazz.classes)

        all
          // Only take out things that have a JSON constructor
          .filter { foundClass ->
            foundClass.constructors.any { constructor ->
              constructor.parameters.any { param ->
                param.getAnnotationsByType(JsonProperty::class.java).isNotEmpty()
              }
            }
          }
          .mapNotNull {
            val name = regex.find(it.name)
            if (name == null) {
              null
            } else {
              val className = name.groupValues[1]
              val packageName = it.name.substring(0, it.name.length - className.length).trim('$')
              LoadedClass(packageName, className)
            }
          }.stream()
      } catch (e: NoClassDefFoundError) {
        return@flatMap listOf<LoadedClass>().stream()
      }
    }.toList().toSet()
  }
}