package de.dennishoersch.util

import com.google.common.base.Optional

package object guava {

  implicit def optionalToOption[T](op: Optional[T]) = if (op.isPresent) Some(op.get) else None
  implicit def optionToOptional[T](op: Option[T]): Optional[T] = if (op.isDefined) Optional.of(op.get) else Optional.absent()
}