/*
    Kotlin Compiler: jvm 1.6.21
    Java bytecode ver 11
 */
package com.gngsn.kotlindemo.decompile

class ValueObject1(     // all args constructor
    val val1: String,   // getter
    val val2: String    // getter
)

/*
public final class ValueObject1 {
   @NotNull
   private final String val1;
   @NotNull
   private final String val2;

   public ValueObject1(@NotNull String val1, @NotNull String val2) {
      Intrinsics.checkNotNullParameter(val1, "val1");
      Intrinsics.checkNotNullParameter(val2, "val2");
      super();
      this.val1 = val1;
      this.val2 = val2;
   }

   @NotNull
   public final String getVal1() {
      return this.val1;
   }

   @NotNull
   public final String getVal2() {
      return this.val2;
   }
}
*/

class ValueObject2(     // all args constructor
    var var1: String,   // getter, setter
    var var2: String    // getter, setter
)
/*
public final class ValueObject2 {
   @NotNull
   private String var1;
   @NotNull
   private String var2;

   public ValueObject2(@NotNull String var1, @NotNull String var2) {
      Intrinsics.checkNotNullParameter(var1, "var1");
      Intrinsics.checkNotNullParameter(var2, "var2");
      super();
      this.var1 = var1;
      this.var2 = var2;
   }

   @NotNull
   public final String getVar1() {
      return this.var1;
   }

   public final void setVar1(@NotNull String <set-?>) {
      Intrinsics.checkNotNullParameter(<set-?>, "<set-?>");
      this.var1 = <set-?>;
   }

   @NotNull
   public final String getVar2() {
      return this.var2;
   }

   public final void setVar2(@NotNull String <set-?>) {
      Intrinsics.checkNotNullParameter(<set-?>, "<set-?>");
      this.var2 = <set-?>;
   }
}
 */

//fun test() {
//    val vo = ValueObject4()
//    val vo2 = ValueObject4("1", "2")
//}

data class ValueObject3(
    // all args constructor, component1, component2, copy, toString, hashCode, equals
    val val1: String,       // getter
    val val2: String,       // getter
)
/*
public final class ValueObject3 {
   @NotNull
   private final String val1;
   @NotNull
   private final String val2;

   public ValueObject3(@NotNull String val1, @NotNull String val2) {
      Intrinsics.checkNotNullParameter(val1, "val1");
      Intrinsics.checkNotNullParameter(val2, "val2");
      super();
      this.val1 = val1;
      this.val2 = val2;
   }

   @NotNull
   public final String getVal1() {
      return this.val1;
   }

   @NotNull
   public final String getVal2() {
      return this.val2;
   }

   @NotNull
   public final String component1() {
      return this.val1;
   }

   @NotNull
   public final String component2() {
      return this.val2;
   }

   @NotNull
   public final ValueObject3 copy(@NotNull String val1, @NotNull String val2) {
      Intrinsics.checkNotNullParameter(val1, "val1");
      Intrinsics.checkNotNullParameter(val2, "val2");
      return new ValueObject3(val1, val2);
   }

   // $FF: synthetic method
   public static ValueObject3 copy$default(ValueObject3 var0, String var1, String var2, int var3, Object var4) {
      if ((var3 & 1) != 0) {
         var1 = var0.val1;
      }

      if ((var3 & 2) != 0) {
         var2 = var0.val2;
      }

      return var0.copy(var1, var2);
   }

   @NotNull
   public String toString() {
      return "ValueObject3(val1=" + this.val1 + ", val2=" + this.val2 + ")";
   }

   public int hashCode() {
      int result = this.val1.hashCode();
      result = result * 31 + this.val2.hashCode();
      return result;
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof ValueObject3)) {
         return false;
      } else {
         ValueObject3 var2 = (ValueObject3)other;
         if (!Intrinsics.areEqual(this.val1, var2.val1)) {
            return false;
         } else {
            return Intrinsics.areEqual(this.val2, var2.val2);
         }
      }
   }
}
*/

data class ValueObject4(    // all args constructor, component1, component2, copy, toString, hashCode, equals
    var var1: String,       // getter, setter
    var var2: String        // getter, setter
)
/*
public final class ValueObject4 {
   @NotNull
   private String var1;
   @NotNull
   private String var2;

   public ValueObject4(@NotNull String var1, @NotNull String var2) {
      Intrinsics.checkNotNullParameter(var1, "var1");
      Intrinsics.checkNotNullParameter(var2, "var2");
      super();
      this.var1 = var1;
      this.var2 = var2;
   }

   @NotNull
   public final String getVar1() {
      return this.var1;
   }

   public final void setVar1(@NotNull String <set-?>) {
      Intrinsics.checkNotNullParameter(<set-?>, "<set-?>");
      this.var1 = <set-?>;
   }

   @NotNull
   public final String getVar2() {
      return this.var2;
   }

   public final void setVar2(@NotNull String <set-?>) {
      Intrinsics.checkNotNullParameter(<set-?>, "<set-?>");
      this.var2 = <set-?>;
   }

   @NotNull
   public final String component1() {
      return this.var1;
   }

   @NotNull
   public final String component2() {
      return this.var2;
   }

   @NotNull
   public final ValueObject4 copy(@NotNull String var1, @NotNull String var2) {
      Intrinsics.checkNotNullParameter(var1, "var1");
      Intrinsics.checkNotNullParameter(var2, "var2");
      return new ValueObject4(var1, var2);
   }

   // $FF: synthetic method
   public static ValueObject4 copy$default(ValueObject4 var0, String var1x, String var2x, int var3, Object var4) {
      if ((var3 & 1) != 0) {
         var1x = var0.var1;
      }

      if ((var3 & 2) != 0) {
         var2x = var0.var2;
      }

      return var0.copy(var1x, var2x);
   }

   @NotNull
   public String toString() {
      return "ValueObject4(var1=" + this.var1 + ", var2=" + this.var2 + ")";
   }

   public int hashCode() {
      int result = this.var1.hashCode();
      result = result * 31 + this.var2.hashCode();
      return result;
   }

   public boolean equals(@Nullable Object other) {
      if (this == other) {
         return true;
      } else if (!(other instanceof ValueObject4)) {
         return false;
      } else {
         ValueObject4 var2x = (ValueObject4)other;
         if (!Intrinsics.areEqual(this.var1, var2x.var1)) {
            return false;
         } else {
            return Intrinsics.areEqual(this.var2, var2x.var2);
         }
      }
   }
}
*/


class ValueObject5(     // all args constructor
) {
    var var1: String? = null   // getter, setter
        set(value) {
            if (value!!.isEmpty())
                throw IllegalArgumentException("must not empty!")
            field = value // field: backing field
        }
    var var2: String? = null    // getter, setter
}