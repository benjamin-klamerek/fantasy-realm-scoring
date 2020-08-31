import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import java.text.Normalizer

/**
 * Extension method to implement then condition like "if" case
 *
 * @param T
 * @param action
 * @return
 */
infix fun <T>Boolean.then(action : () -> T): T? {
    return if (this)
        action.invoke()
    else null
}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(context).inflate(layoutRes, this, attachToRoot)
}

private val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()

/**
 * To remove all accents (replaced by the "basic" letter) in a string
 *
 * @return  new string with replacement done
 */
fun String.normalize(): String {
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.replace(temp, "")
}

