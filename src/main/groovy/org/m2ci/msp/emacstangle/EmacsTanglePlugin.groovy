package org.m2ci.msp.emacstangle

import org.gradle.api.*
import org.gradle.api.tasks.*
import org.gradle.api.provider.*
import org.gradle.api.file.*
import org.gradle.api.tasks.incremental.*

class EmacsTanglePlugin implements Plugin<Project> {

    void apply(Project project) {

        def extension = project.extensions.create('emacs', EmacsTanglePluginExtension, project)

        project.tasks.create('tangle', Tangle) {

            inputDir = extension.inputDir
            outputDir = extension.outputDir

        }

    }

}

class EmacsTanglePluginExtension {

    final Property<File> inputDir
    final Property<File> outputDir

    EmacsTanglePluginExtension(Project project) {

        inputDir = project.objects.property(File)
        inputDir.set(project.buildDir)

        outputDir = project.objects.property(File)
        outputDir.set(project.buildDir)

    }

    void setInputDir(File inputDir) {

        this.inputDir.set(inputDir)

    }

    void setOutputDir(File outputDir) {

        this.outputDir.set(outputDir)
        println this.outputDir.get()

    }

}

class Tangle extends DefaultTask {

    @InputDirectory
    final Property<File> inputDir = project.objects.property(File)

    @OutputDirectory
    final Property<File> outputDir = project.objects.property(File)

    void setInputDir(File inputDir) {

        this.inputDir.set(inputDir)

    }

    void setOutputDir(File outputDir) {

        this.outputDir.set(outputDir)

    }

     @TaskAction
     void tangle(IncrementalTaskInputs inputs) {

         if (!inputs.incremental) {

             project.delete(outputDir.get().listFiles())

          }

         inputs.outOfDate { change ->

            project.exec{

                commandLine "emacs --batch -l org ${change.file} -f org-babel-tangle".tokenize()
                workingDir outputDir.get()

            }

         }

     }

}
