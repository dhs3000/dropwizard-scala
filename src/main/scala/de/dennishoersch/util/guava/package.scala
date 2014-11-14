package de.dennishoersch.util

import com.google.common.base.Optional

package object guava {

  implicit def optionalToOption[A](op: Optional[A]): Option[A] = if (op.isPresent) Some(op.get) else None
  implicit def optionToOptional[A](op: Option[A]): Optional[A] = if (op.isDefined) Optional.of(op.get) else Optional.absent()
}