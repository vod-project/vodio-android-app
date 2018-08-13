package app.vodio.com.vodio.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import app.vodio.com.vodio.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_record_dialog.*
import java.io.FileDescriptor

class BottomRecordFragment : BottomSheetDialogFragment( ) {

    var onRecordFragment = OnRecordFragment()
    var onManageFragment = ManagerRecordFragment()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.bottom_sheet_record_dialog, container, false)
        return view
    }

    override fun onResume() {
        super.onResume()
        onRecordFragment.parentFragment = this
        onManageFragment.parentFragment = this
        updateRecordUIMode()

        tagButton.setOnClickListener{view ->  Toast.makeText(context,"not yet implemented", Toast.LENGTH_SHORT).show()}
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        onRecordFragment.clear()
        onManageFragment.clear()
    }

    fun updateRecordUIMode(){
        showFragment(onRecordFragment, R.id.test)
    }

    fun updateManageUIMode() {
        showFragment(onManageFragment, R.id.test)
    }

    fun setDataSource(url : String){
        onManageFragment.setDataSource(url)
    }
    fun setDataSource(file : FileDescriptor){
        onManageFragment.setDataSource(file)
    }

    fun showFragment(fragment: Fragment, idContainerView: Int) {
        val fm: FragmentManager = childFragmentManager
        val ft: FragmentTransaction = fm.beginTransaction()

        // We can also animate the changing of fragment.
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        // Replace current fragment by the new one.
        ft.replace(idContainerView, fragment)
        // Null on the back stack to return on the previous fragment when user
        // press on back button.
        ft.addToBackStack(null)

        // Commit changes.
        ft.commit()
    }
}