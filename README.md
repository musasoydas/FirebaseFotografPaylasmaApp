# FirebaseFotografPaylasmaApp
kod satırları mevcuttur.incelemek isteyen bakabilir. Arayüzünü güzelleştirebilirim .
Firebase teknolojisi kullanarak kullanıcı girişi ve fotograf paylaşma uygulaması yaptım.Detaya girmem gerekirse ;
LogIn sayfasında bizi Giriş Yap ve Kayıt ol butonları karşılıyor.
Eğer kullanıcı kayıtlı değilse kolay şekilde E-mail ile kayıt olabiliyor.Emaili girip Şifresini girdiğinde 
eğer şifresi 6 karakterden düşük ise hata mesajını Toast ile kullanıcıya gösteriyor.Bu hata ayıklamasını ise Firebase'nin hata ayıklaması ile yaptım.Eğer 6 karakterden 
büyük ise kullanıcı başarılı şekilde giriş yapar ve Firabase Authentication bölümüne kullanı başarılı şekilde kaydediliyor.

Eğer kullanıcı kayıtlı ise kullanıcı emeaili ve şifresini girer. Şifrede hata var ise hata mesajını Toast ile kullanıcıya gösteriyor.Bu hata ayıklamasını ise Firebase'nin hata ayıklaması ile 
yaptım.Eğer şifre doğru ise kimin girdiğini anlayıp hesabın kullanıcı adını alarak Toast mesajı ile "Hoşgeldiniz name" demesini sağladım.

Anasayfamda bu zamana kadar yüklenen fotograflar ve yorumları en yeniden en eskiye sıralanmış şekilde gösterilmesini sağladım.
menu olarak fotograf paylas seçeneği koyup bunu feed activtye bağladım.tıklandığında fotograf paylaş Activitye gitmektedir.Fotograf seçmek istediğinde ise dangerous izin
olduğundan dolayı kullanıcıdan izin isteme metodunu kullandım.Ayrıcıda burda sectiği fotografı ve yorumu firebase de veritabanında sakladım

Çıkış yapmaya tıkladığında kullanıcının çıkış yaptığını anlayıp bir daha girdiğinde logın sayfasını karşılamasını sağladım.Çıkış yapmadan uygulamayı kapatmış ise bunu 
algılayıp FeedActivitye yönlendirdim



![reesim](https://bit.ly/3GhHa2E)
![reesim](https://bit.ly/3GjFTZc)
![reesim](https://bit.ly/3I5faAH)
![reesim](https://bit.ly/3C27YBE)
![reesim](https://bit.ly/3HXMsSk)
![reesim](https://bit.ly/3WDRE1O)
![reesim](https://bit.ly/3VkvPDj)
![reesim](https://bit.ly/3FT9Wp4)
