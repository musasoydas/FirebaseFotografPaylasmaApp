package com.example.fotografpaylasmauygulamasi

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_fotograf_paylasma.*
import java.util.*

// 30 Manifestte gorsel için izin verildi
//41 firebaseden artık  firestore database ve stronge yi etkimleştiriyoruz

class FotografPaylasmaActivity : AppCompatActivity() {

    //39 gerekli değişkenleri tanımlıyoruz
    var secilenGorsel: Uri? =null
    var secilenBitmap: Bitmap?=null

    //42 depo işlemlerini yapmamız için tanımlamamızı yapıyoruz.(resimi alıp depoya gönderecez)
    private lateinit var storage: FirebaseStorage
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_fotograf_paylasma)
        //43 proje başladığında oluşsun
        storage=FirebaseStorage.getInstance()
        auth=FirebaseAuth.getInstance()
        database=FirebaseFirestore.getInstance()

    }
    fun paylas(view:View)
    {
        var uuid=UUID.randomUUID()//48
        var gorselIsmi="${uuid}.jpg "//48

        var reference=storage.reference // 44 buradaki referance aslında bizim firebase sayfasındaki storage sayfasına işaret ediyor

        // var gorselreference=reference.child("images").child("secilengorsel.jpg") // 45 storage sayfasındaki images klasöründeki secilen gorsel.jpg resmine referans veririm diyor
        //ama biz bunu dinamik olsun diye uuid olarak tanımlayacaz

        var gorselreference=reference.child("images").child(gorselIsmi)//49 storage sayfasındaki images klasöründeki uuid nin üretiği id ile depomuza kaydeder

        //46
        if(secilenGorsel!=null)
        {
            gorselreference.putFile(secilenGorsel!!).addOnSuccessListener {
               //başarılı ise çalışacak kodlar

                val yuklenenGorselReference=FirebaseStorage.getInstance().reference.child("images").child(gorselIsmi)//50 şimdi biz gorseli kaydettik ama url si lazım ki biz onu alıp
                 // veri tabanına eklmemiz gerekiyor
                 yuklenenGorselReference.downloadUrl.addOnSuccessListener {
                     val downloadUrl=it.toString()

                    //51 artık veri tabanına kaydetmmeiz gerekiyor
                    var guncelKullaniciEmail=auth.currentUser!!.email.toString() // 52 fotografı kim paylaşmış?
                     var kullaniciYorumu=yorumText.text.toString()// 53 yorumu ne yapmış?
                     var tarih=Timestamp.now() //!!! 54 Timestamp zaman alıyor ama firibase olanı seç!!!

                    var postHashMap= hashMapOf<String,Any>()// 55 veri tabanı için nesne ürettik ve anahatar için String: , any yazmamız amacı ise her türlü veriyi desteklmesi
                    postHashMap.put("gorselurl",downloadUrl)
                    postHashMap.put("kullaniciemail",guncelKullaniciEmail)
                    postHashMap.put("kullaniciyorum",kullaniciYorumu)
                    postHashMap.put("tarih",tarih)

                     //56 veritabanına gönderdik
                     database.collection("Post").add(postHashMap).addOnCompleteListener {
                         if(it.isSuccessful)
                         {
                             finish() //diyebiliriz çünkü bu aktiviteye gelirken feed aktiivityde finish yapmadık
                             /*
                             var intent=Intent(this,FeedActivity::class.java)
                             startActivity(intent)*/
                         }
                     }.addOnFailureListener {
                         Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                     }

                }
            }.addOnFailureListener {
                //hata varsa ne hatası olduğunu gösteren kodlar
                println(it.localizedMessage)
            }
            //47 eğerki hata alırsan veya başarılı şekilde kaydetmezse firebaseden storage saygasına gelip rules alttaki kod bloğunu yapıştır
            /*
                rules_version = '2';
                service firebase.storage {
                match /b/{bucket}/o {
                match /{allPaths=**} {
                // Allow access by all users
                  allow read, write;
                }
              }
            }
            */
        }
    }
    fun gorselSec(view:View)
    {
 // 31 gorsel iznini manifeste yazdık ama DANGEROUS olduğundan kullanıcıdan da almamız gerekmektedir
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED)// 32 izin verilmediyse hiç
        {
            ActivityCompat.requestPermissions(this,arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)//33 izni iste
        }
        else // 34 zaten izin verildiyse galeriye git
        {
            val galeriIntent =Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult( galeriIntent,2)
        }
    }
    //35 yukarıda verdiğimiz requestkodları deneyeceğiz
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if(requestCode == 1)
        {
            if(grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED)
            {
                //36 izin verilince yapılacaklar
                val galeriIntent =Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult( galeriIntent,2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
//37 galeri işlemleri
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode==2 && resultCode==Activity.RESULT_OK && data!=null)//38 requestcode 2 ve galeriye gidip resmi seçtiyse ve seçtiği resim boş değilse
        {
            secilenGorsel=data.data//40 buraların hepsi klasik resim alma komutları
            if(secilenGorsel != null)
            {
                if(Build.VERSION.SDK_INT>=28)
                {
                    var source=ImageDecoder.createSource(this.contentResolver,secilenGorsel!!)
                    secilenBitmap=ImageDecoder.decodeBitmap(source)
                    imageView.setImageBitmap(secilenBitmap)
                }
                else{
                    secilenBitmap=MediaStore.Images.Media.getBitmap(this.contentResolver,secilenGorsel)
                    imageView.setImageBitmap(secilenBitmap)
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
