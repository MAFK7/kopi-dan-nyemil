package lat.pam.kopidannyemil

import android.graphics.Color
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

fun BottomSheetDialogFragment.styleSheet() {
    val d = dialog as? BottomSheetDialog ?: return
    val sheet = d.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet) ?: return
    sheet.setBackgroundColor(Color.TRANSPARENT)
    val behavior = BottomSheetBehavior.from(sheet)
    behavior.skipCollapsed = true
    behavior.state = BottomSheetBehavior.STATE_EXPANDED
}