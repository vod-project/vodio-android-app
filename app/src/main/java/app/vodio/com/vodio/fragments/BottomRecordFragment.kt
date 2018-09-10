package app.vodio.com.vodio.fragments

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import app.vodio.com.vodio.R
import app.vodio.com.vodio.beans.Vod
import app.vodio.com.vodio.database.retrofit.repositories.LoginRepository
import app.vodio.com.vodio.database.retrofit.repositories.VodRepository
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.android.synthetic.main.bottom_sheet_record_dialog.*
import kotlinx.android.synthetic.main.manage_vod_after_record.*
import kotlinx.android.synthetic.main.record_vod_layout.*
import java.io.File

class BottomRecordFragment : BottomSheetDialogFragment(), View.OnClickListener {
    private var onRecordFragment = OnRecordFragment()
    private var onManageFragment = ManagerRecordFragment()

    var vod : Vod? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.bottom_sheet_record_dialog, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onRecordFragment.parentFragment = this
        onManageFragment.parentFragment = this
    }

    override fun onResume() {
        super.onResume()
        if(vod == null)
            setRecordUIMode()
        tagButton.setOnClickListener{view ->  Toast.makeText(context,"not yet implemented", Toast.LENGTH_SHORT).show()}
    }

    override fun onClick(v: View?) {
        when(v!!.id){
            retryVod?.id -> retryVod()
            modVodButton?.id -> Toast.makeText(context, "not yet implemented", Toast.LENGTH_SHORT).show()
            sendVodButton?.id -> performSendVod()
            recordVodBSF?.id -> performStopRecord()
        }
    }

    fun performStopRecord(){
        onRecordFragment.stopRecord()
        setDataSource(onRecordFragment.getFile())
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        onRecordFragment.clear()
        onManageFragment.clear()
        Log.w(this.javaClass.simpleName,"dismiss")
    }

    fun setDataSource(file : File?){
        onManageFragment.setDataSource(file)
        setManageUIMode()
    }

    private fun getTitleText() : String{
        return titleEditText.text.toString()
    }


    private fun setRecordUIMode(){
        showFragment(onRecordFragment, R.id.test)
        Log.w(this.javaClass.simpleName,"show on record fragment")
    }

    private fun setManageUIMode() {
        showFragment(onManageFragment, R.id.test)
        Log.w(this.javaClass.simpleName,"show on manage fragment")
    }

    private fun retryVod(){
        setRecordUIMode()
        vod = null
    }

    private fun performSendVod(){
        if(vod != null) {
            val author = LoginRepository.getInstance(context!!).getLoggedUser().value
            vod?.authorLogin = author!!.login!!
            val titleText = getTitleText()
            vod?.title = if(titleText.isEmpty()) "no-title" else titleText
            vod?.initTimestamp()

            VodRepository.getInstance(context!!).createVod(vod!!).observe(this, Observer {
                if(it!!){
                    Toast.makeText(context, "created", Toast.LENGTH_SHORT).show()
                    dismiss()
                }else{
                    Toast.makeText(context, "create fail", Toast.LENGTH_SHORT).show()
                }
            })
        }else{
            Log.v(javaClass.simpleName,"data source null")
        }
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