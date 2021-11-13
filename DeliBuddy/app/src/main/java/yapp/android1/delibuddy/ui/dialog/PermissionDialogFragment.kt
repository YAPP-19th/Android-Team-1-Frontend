package yapp.android1.delibuddy.ui.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentActivity

class PermissionDialogFragment(private val fragmentActivity: FragmentActivity) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val builder: AlertDialog.Builder = activity?.let {
//            AlertDialog.Builder(it)
//        } ?: throw IllegalStateException("Activity cannot be null")

        val builder = AlertDialog.Builder(fragmentActivity)

        builder.apply {
            setTitle("권한 설정 필요")
            setMessage("편리한 딜리버디 이용을 위해 권한을 허용해 주세요\n[설정] > [권한]에서 사용으로 활성화해 주세요")

            setPositiveButton("설정",
                DialogInterface.OnClickListener() { dialog, which ->
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:" + activity?.packageName)
                    )
                    intent.addCategory(Intent.CATEGORY_DEFAULT)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                })

            setNegativeButton("닫기",
                DialogInterface.OnClickListener { dialog, which ->
                    dismiss()
                })
        }

        return builder.create()
    }
}