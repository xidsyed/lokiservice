package org.example.project.loki.core

@Target(
    allowedTargets = [
        AnnotationTarget.CLASS,
        AnnotationTarget.PROPERTY,
        AnnotationTarget.FUNCTION,
        AnnotationTarget.TYPEALIAS,
    ],
)
@RequiresOptIn(level = RequiresOptIn.Level.ERROR)
public annotation class InternalLokiApi
