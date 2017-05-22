package glass.phil.auto.moshi

import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind.BOOLEAN
import javax.lang.model.type.TypeKind.BYTE
import javax.lang.model.type.TypeKind.CHAR
import javax.lang.model.type.TypeKind.DECLARED
import javax.lang.model.type.TypeKind.DOUBLE
import javax.lang.model.type.TypeKind.FLOAT
import javax.lang.model.type.TypeKind.INT
import javax.lang.model.type.TypeKind.LONG
import javax.lang.model.type.TypeKind.SHORT
import javax.lang.model.type.TypeMirror

val TypeMirror.defaultValue get() = when (kind) {
  BOOLEAN -> "false"
  CHAR, BYTE, SHORT, INT, LONG, FLOAT, DOUBLE -> "0"
  else -> "null"
}

val TypeMirror.parameterized get() = kind == DECLARED && (this as DeclaredType).typeArguments.isNotEmpty()
