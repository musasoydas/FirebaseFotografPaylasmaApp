package com.example.fotografpaylasmauygulamasi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_feed.*

class FeedActivity : AppCompatActivity() {

    //25 firebase nesnesi oluşturalım
    private lateinit var auth:FirebaseAuth
    //57
    private lateinit var database: FirebaseFirestore

    //62 post clasından liste nesnesi oluşuruyoruz
    var postListesi=ArrayList<Post>()

    //72
    private lateinit var recyclerViewAdapter: FeedRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feed)

        //26 activity başladığında başlasın
        auth=FirebaseAuth.getInstance()
        //58 activity başladığında başlasın
        database=FirebaseFirestore.getInstance()

        //63 ekstra
        verileriAl()


        //73
        var layoutManager=LinearLayoutManager(this)
        recyclerView.layoutManager=layoutManager
        recyclerViewAdapter= FeedRecyclerViewAdapter(postListesi)
        recyclerView.adapter=recyclerViewAdapter


    }
    //23 yaptığımız menuyu bağlayalım
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val menuInflater=menuInflater
        menuInflater.inflate(R.menu.options_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    //24 menude hangisini seçtiğimizi anlayacak fonksiyon
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId==R.id.fotograf_paylas)
        {
            // 28 fotoğraf paylaşıldı aktivitesine gididilecek
            val intent=Intent(this,FotografPaylasmaActivity::class.java)
            startActivity(intent)


            ////////////29 FotoğrafpaylaşmaActivity tasarlandı  ve oraya gidildi

        }
        else if(item.itemId==R.id.cikis_yap)//çıkıs yapa basılınca olacak kodlar
        {//biz main activitye gitmek istiyoruz

            auth.signOut()// 27firabaseden de çıkış yapmaya yarar

            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
            finish()


        }
        return super.onOptionsItemSelected(item)
    }
    //59 veriyi çekmek için fonksiyon
    fun verileriAl()
    {
        //Firebase de oluşturduğun  collectionun aynısını yazman gerekiyor:Post bizimki öyle kaydettik çünkü
        database.collection("Post").orderBy("tarih",Query.Direction.DESCENDING).addSnapshotListener { snapshot, exception ->
            //.orderBy("tarih",Query.Direction.DESCENDING) sıralama yapmaya yarıyor .tarih bak ve DESCENDING(en yeni atılan en başta çıksın)
            if(exception!= null )//eğer ki hata varsa exception:hata varsa demektir
            {
                Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_SHORT).show()
            }
            else// hata yoksa
            {
                if(snapshot!=null)// alacağımız veri boş değilse
                {
                    if(snapshot.isEmpty==false)//içi boş değilse
                    {
                        val documents=snapshot.documents
                        //64 listeyi temzile
                        postListesi.clear()
                        for(document in documents)//document bir liste geri döndürüyor bunun içindeki verileri lamak için for döngüsünü kullandık
                        {
                            var kullaniciEmail=document.get("kullaniciemail") as String //cash yaptık any geri döndürüyotdu ama biz bunu String yapabiliriz
                            var kullaniciYorum=document.get("kullaniciyorum") as String
                            var gorselUrl=document.get("gorselurl") as String

                            // 60 kullanıcıları hepsini bir listede tutmak için ayrı bir class oluşturup
                            // listete ekliyoruz class adı Post

                            //63
                            val indirilenPost=Post(kullaniciEmail,kullaniciYorum,gorselUrl)
                            postListesi.add((indirilenPost))

                            //64 recycler view oluşturmak için res>new >LayoutResourceFile
                            //65 recycler view xmlini oluştur
                            //66 FeedRecyclerViewAdapter oluşturduk
                        }
                        //74 yeni veri gelince kendini yeniler
                        recyclerViewAdapter.notifyDataSetChanged()
                        //75 gorselleri gosterebilmek için dış kütüphanye gerek var (bunun için picasso kütüphanesini kullanacağız)
                        //76 imlement et ardından adaptere git
                        // implementation 'com.squareup.picasso:picasso:2.8'
                    }
                }
            }

        }
    }
}