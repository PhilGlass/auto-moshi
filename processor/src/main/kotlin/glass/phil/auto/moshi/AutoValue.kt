package glass.phil.auto.moshi

import com.google.auto.value.extension.AutoValueExtension
import javax.lang.model.util.Types

val AutoValueExtension.Context.types: Types get() = processingEnvironment().typeUtils
