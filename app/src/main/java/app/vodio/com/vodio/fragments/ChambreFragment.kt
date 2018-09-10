package app.vodio.com.vodio.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.vodio.com.vodio.R

class ChambreFragment : AbstractFragment() {
    override fun getIconId() : Int{
        return iconMapping.idIcon
    }

    object iconMapping{
        const val idIcon = R.id.bedroom_navbar
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chambre, container, false)
    }

    override fun onClick(view: View) {

    }
}
