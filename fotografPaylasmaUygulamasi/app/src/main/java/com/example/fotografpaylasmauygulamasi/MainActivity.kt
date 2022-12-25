package com.example.fotografpaylasmauygulamasi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth // 1 firebase kullanmamızı sağlayacak nesneyi oluşturuyotuz **

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        auth = FirebaseAuth.getInstance() // 2 proje başlarken oluşturulmasını sağlar **

        //20 kullanıcı giriş yaptı ancak uygulamadan çıkınca tekrar logın sayfasına gitmemesini engellemke için
        val guncelkullanici=auth.currentUser
        if(guncelkullanici!=null)// 21 önceden giriş yapmadıysa null gelir ancak giriş yaptıysa null değildir
        {
            val intent = Intent(this, FeedActivity::class.java)
            startActivity(intent)
            finish()

        }
        // 22 menu oluşturma işlemine geçiyoruz res>sağ>directory>menu yaz>ardından
        //menu klasörü>sağ> menu resourse> adını gir>itemden özelliklerini ekle > feed aktivteye git
    }


    // KULLANICININ KAYIT OLMASI İÇİN GEREKLİ KODLAR
    fun kayitOl(view: View)
    {
        val email = emailText.text.toString() //3 girilen emaili alıyoruz
        val sifre = paswordText.text.toString() // 4 girilen sifreyi alıyoruz
        //alt tarafta  auth.createUserWithEmailAndPassword(email,sifre) sonra koyulan noktadan sonraki listener anlamları
        // addOnCanceledListener = iptal edildi listener
        // addOnComplateListener= tamamlandı listener,işlem bitti listener
        // addOnFailureListener=hata alındı listener
        // addOnSuccessListener= başarılı listenerü

        //5 email ve pasword ile kayıt oluşturma . eğer kayıt olma tamamlandığında ne olsun
        auth.createUserWithEmailAndPassword(email,sifre).addOnCompleteListener {
            if(it.isSuccessful)// 6 eğer başarılı ise ne olsun
            {
                // 7 diğer aktivteye geçme kodları
                val intent = Intent(this, FeedActivity::class.java)
                startActivity(intent)
                finish() //  8 kullanıcı siteye giridkten sonra neden kullanıcı login sayfasına gidebilsin?
            }
            // 9 kayır olmada hata olursa devreye giren listener
        }.addOnFailureListener {
            Toast.makeText(this,it.localizedMessage,Toast.LENGTH_LONG).show();//10 hatayı toast ile kullanıcaya verir
        }

    }

    //11 Giriş yapabilmesi için gerekli kodlar
    fun girisYap(view: View)
    {
        val email=emailText.text.toString()//12 giris için emaili aldık
        val sifre=paswordText.text.toString()// 13 giris için şifre aldık
        //14 firebase de öyle bir kayıt var mı kotrol eden hazır fonksiyon
        auth.signInWithEmailAndPassword(email,sifre)
            .addOnCompleteListener {
            //15 eğer tamamlandıysa ne olsun,
                if(it.isSuccessful)//16 giris başarıylsa ne olsun
                {
                    //18 güncel kullanıcıyı almak için  hoşgeldiniz toast yapmak
                    val guncelkullanici=auth.currentUser?.email.toString()
                    Toast.makeText(this, "Hoşgeldin ${guncelkullanici}", Toast.LENGTH_SHORT).show()
                    //17 diğer akktviteye gitsin
                    val intent = Intent(this, FeedActivity::class.java)
                    startActivity(intent)
                    finish() //  18 kullanıcı siteye giridkten sonra neden kullanıcı login sayfasına tekrar gidebilsin?



                }
        }.addOnFailureListener {
                //19 giriş yapmada sıkıntı varsa hatayı söyle !
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
            }
    }
}