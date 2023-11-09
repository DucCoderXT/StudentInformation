package net.braniumacademy.exercises515.ui.updating.dialog

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import net.braniumacademy.exercises515.R
import net.braniumacademy.exercises515.ui.updating.ActionUpdateListener

class ConfirmDialogFragment(
    private val listener: ActionUpdateListener
) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setMessage(getString(R.string.msg_update_confirm))
            .setPositiveButton(getString(android.R.string.ok)) { _, _ ->
                listener.onConfirm()
            }.setNegativeButton(getString(android.R.string.cancel)) { _, _ ->
                listener.onCancel()
            }.create()
    }
}