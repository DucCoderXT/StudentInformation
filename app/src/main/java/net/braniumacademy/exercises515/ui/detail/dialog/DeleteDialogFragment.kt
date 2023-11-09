package net.braniumacademy.exercises515.ui.detail.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import net.braniumacademy.exercises515.R
import net.braniumacademy.exercises515.ui.base.ActionListener

class DeleteDialogFragment(
    private val listener: ActionListener
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.msg_delete_confirm))
            .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                listener.onConfirm()
            }.setNegativeButton(getString(android.R.string.cancel)) { _, _ ->
                listener.onCancel()
            }.create()
    }
}