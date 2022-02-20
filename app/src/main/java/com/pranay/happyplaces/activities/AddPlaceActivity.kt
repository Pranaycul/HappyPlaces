package com.pranay.happyplaces.activities

import android.Manifest

import android.app.DatePickerDialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.ContextWrapper

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.pranay.happyplaces.R
import com.pranay.happyplaces.databinding.ActivityAddPlaceBinding
import java.io.*
import java.text.SimpleDateFormat
import java.util.*


@Suppress("DEPRECATION")
class AddPlaceActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddPlaceBinding

    private lateinit var saveImageToInternalStorage: Uri

    private var cal = Calendar.getInstance()

    private lateinit var dateSetListener: DatePickerDialog.OnDateSetListener

    private val openGalleryLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                val selectedImage: Uri = (result.data!!.data) as Uri
                try {
                    val imageStream: InputStream? = contentResolver.openInputStream(selectedImage)

                    val yourSelectedImage: Bitmap = BitmapFactory.decodeStream(imageStream)
                    saveImageToInternalStorage=saveImageToInternalStorage(yourSelectedImage)
                    imageStream?.close()
                    Log.i("ImagePath::","$saveImageToInternalStorage")
                    binding.ivImage.setImageBitmap(yourSelectedImage)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace(); }

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPlaceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbarAddPlace)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbarAddPlace.setNavigationOnClickListener {
            onBackPressed()
        }


        dateSetListener =
            DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                cal.set(Calendar.YEAR, year)
                cal.set(Calendar.MONTH, monthOfYear)
                cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

                updateDateInView()
            }

        binding.etDate.setOnClickListener(this)
        binding.tvAddImage.setOnClickListener(this)

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CAMERA && resultCode == RESULT_OK) {
            val imageBitmap = data!!.extras!!.get("data") as Bitmap
            saveImageToInternalStorage= saveImageToInternalStorage(imageBitmap)
            Log.i("ImagePath::","$saveImageToInternalStorage")
            binding.ivImage.setImageBitmap(imageBitmap)
        }
    }


    private fun updateDateInView() {
        val myFormat = "dd.MM.yyyy" // mention the format you need
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault()) // A date format
        binding.etDate.setText(
            sdf.format(cal.time).toString()
        )
    }

    private fun choosePhotoFromGallery() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    // Here after all the permission are granted launch the gallery to select and image.
                    if (report!!.areAllPermissionsGranted()) {

                        val galleryIntent = Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                        )
                        openGalleryLauncher.launch(galleryIntent)
                        // startActivityForResult(galleryIntent, AddHappyPlaceActivity.GALLERY)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread()
            .check()
    }
    private fun takePhotoFromCamera() {

        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport?) {

                    if (report!!.areAllPermissionsGranted()) {
                        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        try {
                            startActivityForResult(takePictureIntent, CAMERA)
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                        }

                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: MutableList<PermissionRequest>?,
                    token: PermissionToken?
                ) {
                    showRationalDialogForPermissions()
                }
            }).onSameThread()
            .check()
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.et_date -> {
                DatePickerDialog(
                    this@AddPlaceActivity,
                    dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)
                ).show()
            }
            R.id.tv_add_image -> {
                val pictureDialog = AlertDialog.Builder(this)
                pictureDialog.setTitle("Select Action")
                val pictureDialogItems =
                    arrayOf("Select photo from gallery", "Capture photo from camera")
                pictureDialog.setItems(
                    pictureDialogItems
                ) { _, which ->
                    when (which) {
                        // Here we have create the methods for image selection from GALLERY
                        0 -> choosePhotoFromGallery()
                        1 -> takePhotoFromCamera()
                    }
                }
                pictureDialog.show()
            }
        }
    }

    private fun showRationalDialogForPermissions() {
        AlertDialog.Builder(this)
            .setMessage("It Looks like you have turned off permissions required for this feature. It can be enabled under Application Settings")
            .setPositiveButton(
                "GO TO SETTINGS"
            ) { _, _ ->
                try {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                    val uri = Uri.fromParts("package", packageName, null)
                    intent.data = uri
                    startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    e.printStackTrace()
                }
            }
            .setNegativeButton("Cancel") { dialog,
                                           _ ->
                dialog.dismiss()
            }.show()
    }
    private fun saveImageToInternalStorage(bitmap: Bitmap): Uri {
        val wrapper = ContextWrapper(applicationContext)


        var file = wrapper.getDir(IMAGE_DIRECTORY, Context.MODE_PRIVATE)


        file = File(file, "${UUID.randomUUID()}.jpg")

        try {

            val stream: OutputStream = FileOutputStream(file)


            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)


            stream.flush()

            stream.close()
        } catch (e: IOException) { // Catch the exception
            e.printStackTrace()
        }


        return Uri.parse(file.absolutePath)
    }
    companion object {

        private const val CAMERA = 2
        private const val IMAGE_DIRECTORY = "HappyPlacesImages"
    }
}