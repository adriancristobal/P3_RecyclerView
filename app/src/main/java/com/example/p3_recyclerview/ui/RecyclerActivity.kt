package com.example.p3_recyclerview.ui

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.example.p3_recyclerview.R
import com.example.p3_recyclerview.databinding.InsertDataPersonBinding
import com.example.p3_recyclerview.databinding.RecyclerMainBinding
import com.example.p3_recyclerview.domain.model.Person
import com.example.p3_recyclerview.domain.usecases.*
import com.example.p3_recyclerview.ui.detailsPerson.DetailsPersonActivity
import com.example.p3_recyclerview.utils.StringProvider
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import timber.log.Timber


class RecyclerActivity : AppCompatActivity() {

    private lateinit var binding: RecyclerMainBinding

    private val viewModel: RecyclerViewModel by viewModels {
        RecyclerViewModelFactory(
            StringProvider(this),
            UsecaseGetAllPersons(),
            UsecaseGetPerson(),
            UsecaseDeletePerson(),
            UsecaseAddPerson(),
            UsecaseValidatedPerson(),
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = RecyclerMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //cargar imagen
        chargeImage()
        //cambio de estado ViewModel
        changesStatus()
        //botones
        setOnClickListener()
    }

    /** la funcion onStart hace que cada vez que se carga este activity, ocurra lo que tiene dentro (igual que onCreate).
     * La diferencia es que el onStart tambien sirve si el usuario le da al boton de atras del movil
     */
    override fun onStart() {
        super.onStart()
        //mostrar personas iniciales
        showList()
    }

    private fun changesStatus() {
        viewModel.uiState.observe(this@RecyclerActivity) { state ->

            if (state.message != null){
                Toast.makeText(this@RecyclerActivity, state.message, Toast.LENGTH_SHORT).show()
                viewModel.errorMostrado()
                Timber.tag("::TIMBER").i(state.message)
            } else {
                with(binding) {

                    val listPerson: List<Person> = state.listPerson

                    listPerson.let {
                        rvPersons.addItemDecoration(
                            DividerItemDecoration(
                                rvPersons.context,
                                LinearLayoutManager.HORIZONTAL
                            )
                        )
                        rvPersons.adapter = PersonAdapter(it, ::onDeletePerson, ::onShowPersonDetails)
                        PersonAdapter(it, ::onDeletePerson, ::onShowPersonDetails).refreshList(it)
                        rvPersons.layoutManager = LinearLayoutManager(this@RecyclerActivity)
                    }
                }
            }

        }
    }

    private fun setOnClickListener() {
        with(binding){
            btnAddFloating.setOnClickListener {
                floatingBtnAdd()
            }
        }
    }

    private fun showList() {
        viewModel.showList()
    }



    private fun floatingBtnAdd(){
        showPersonalizeDialog()
    }

    private fun showPersonalizeDialog() {

        val builder: AlertDialog.Builder = AlertDialog.Builder(this@RecyclerActivity, R.style.my_dialog)
        val inflater: LayoutInflater = layoutInflater
        val view: View = inflater.inflate(R.layout.insert_data_person, null)
        val bindingDialog = InsertDataPersonBinding.bind(view);
        builder.setView(view)

        val dialog: AlertDialog = builder.create()
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()


        val buttonAdd: Button = view.findViewById(R.id.btnAdd)

        buttonAdd.setOnClickListener() {
            with(bindingDialog) {
                val name = etName.editText?.text.toString()
                val password = etPassword.editText?.text.toString()
                val phone = etPhone.editText?.text.toString()
                val p = Person(name, password, phone.toInt())
                viewModel.addPerson(p)
                dialog.dismiss()
            }
        }
    }

    private fun onShowPersonDetails(position: Int) {
        val intent =  Intent(this@RecyclerActivity, DetailsPersonActivity::class.java)
        intent.putExtra("positionPerson", position)
        startActivity(intent)
    }

    private fun onDeletePerson(position: Int) {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("CONFIRMATION")
            .setMessage("Are you sure to delete this person?")
            .setNegativeButton("NO") { view, _ ->
                view.dismiss()
            }
            .setPositiveButton("YES") { view, _ ->
                viewModel.deletePerson(position)
                //binding.rvPersons.adapter?.notifyItemRemoved(position)
                view.dismiss()
            }
            .setCancelable(false)
            .create()

        dialog.show()
    }

    private fun chargeImage() {
        //este metodo no funsiona
        //binding.imageView.load(assets.open("pepinilloYcacahuete.jpg"))
        binding.imageView.load(Uri.parse("file:///android_asset/pepinilloYcacahuete.jpg"))
    }

    /** Esto no me funciona */
    /*override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        val dialog = MaterialAlertDialogBuilder(this)
            .setTitle("CONFIRMATION")
            .setMessage("Do you want to exit the application?")
            .setNegativeButton("NO") { view, _ ->
                view.dismiss()
            }
            .setPositiveButton("YES") { view, _ ->
                intent = Intent(Intent.ACTION_MAIN)
                intent.addCategory(Intent.CATEGORY_HOME)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
                view.dismiss()
            }
            .setCancelable(true)
            .create()
        if (keyCode == event?.keyCode) {
            dialog.show()
        }

        return super.onKeyDown(keyCode, event)
    }

     */
}