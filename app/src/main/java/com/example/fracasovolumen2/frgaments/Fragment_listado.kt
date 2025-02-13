package com.example.fracasovolumen2.frgaments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fracasovolumen2.Comunicador
import com.example.fracasovolumen2.R
import com.example.fracasovolumen2.database.DataBaseHelper
import com.example.fracasovolumen2.datos.Bar

class Fragment_listado : Fragment() {

    private lateinit var db: DataBaseHelper
    private lateinit var listado: ListView
    private lateinit var listadoBares: MutableList<Bar>
    private var comunicador: Comunicador? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)

        comunicador = context as? Comunicador
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val vista = inflater.inflate(R.layout.fragment_listado, container, false)


        return vista
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listado = view.findViewById(R.id.listView_baresListado_fragment)

        db = DataBaseHelper(requireContext())
        listadoBares = db.obtenerTodosLosBares()

        val adaptadpr = AdaptadorListFragment(
            requireContext(),
            R.id.listView_baresListado_fragment,
            listadoBares
        )
        listado.adapter = adaptadpr

        listado.setOnItemClickListener { adapterView, view, i, l ->
            val barSelecionado = listadoBares[i]
            Toast.makeText(requireContext(), " id es ${barSelecionado.idBar}", Toast.LENGTH_SHORT)
                .show()

            // comprueba que el id del bar no sea nuleo, en caso hace el envio
            barSelecionado.idBar?.let { comunicador?.envioDeIds(it) }

            listado.adapter = adaptadpr
        }


    }

    fun actualizarId(id: Int) {
        listadoBares = db.obtenerTodosLosBares()
    }

    private inner class AdaptadorListFragment(
        context: Context,
        resource: Int,
        listado: MutableList<Bar>
    ) : ArrayAdapter<Bar>(context, resource, listado) {

        override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
            return crecarFilaPersonal(position, convertView, parent)
        }

        override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
            return crecarFilaPersonal(position, convertView, parent)
        }

        // funcion para crear las filas personalizadas-- siempre que no sea un solo un textView
        fun crecarFilaPersonal(position: Int, convertView: View?, parent: ViewGroup): View {
            // definir la vista
            val vista = convertView ?: LayoutInflater.from(context)

            // lisgar la vista personal a la list view
            val filaVista =
                convertView ?: layoutInflater.inflate(R.layout.item_listado, parent, false)
            val bar = listadoBares[position]
            filaVista.findViewById<TextView>(R.id.txtV_nombreBarListado).text = bar.nombre
            val txtWeb = filaVista.findViewById<TextView>(R.id.txtV_webBarListado)
            txtWeb.text = bar.web
            txtWeb.setOnClickListener {
                var url = bar.web

                // comprobacion de protocoloes web de la url
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "https://$url"
                }
                if (!url.isNullOrEmpty()) {

                    /* //intent normal que al pinchar lleva al navegador
                    val intentNavegador = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    context.startActivity(intentNavegador)
                    */

                    // intent con un chooser
                    val intentChoorser = Intent().apply {
                        action = Intent.ACTION_VIEW
                        type="text/html"
                    }

                    val chooser = Intent.createChooser(intentChoorser, "Abrir con:")
                    context.startActivity(chooser)
                }
            }
            return filaVista
        }
    }

}