    package com.arifmuratpoyraz.purepage.view

    import android.annotation.SuppressLint
    import androidx.appcompat.app.AppCompatActivity
    import android.os.Bundle
    import android.util.Log
    import android.widget.ImageView
    import android.widget.Toast
    import androidx.appcompat.widget.AppCompatImageButton
    import androidx.recyclerview.widget.LinearLayoutManager
    import androidx.recyclerview.widget.RecyclerView
    import com.arifmuratpoyraz.purepage.model.Post
    import com.arifmuratpoyraz.purepage.adapter.PostRecyclerAdapter
    import com.arifmuratpoyraz.purepage.R
    import com.arifmuratpoyraz.purepage.model.Singleton
    import com.google.firebase.firestore.DocumentSnapshot
    import com.google.firebase.firestore.FirebaseFirestore
    import com.google.firebase.firestore.Query

    class HomeActivity : AppCompatActivity() {

        val bottomsheetfragment = BottomSheetFragment()
        private lateinit var menuButton : ImageView
        private lateinit var database : FirebaseFirestore
        private lateinit var recyclerAdapter: PostRecyclerAdapter
        var postListesi = ArrayList<Post>()
        var lastVisiblePost: DocumentSnapshot? = null
        var isLastItemReached = false
        var nick = Singleton.nick

        @SuppressLint("MissingInflatedId")
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_home)
            database = FirebaseFirestore.getInstance()
            val recyclerView : RecyclerView = findViewById(R.id.homeRecyclerView)
            menuButton = findViewById(R.id.menuButton)

            println(nick)
            verileriAl()

            var layoutManager = LinearLayoutManager(this)
            recyclerView.layoutManager = layoutManager
            recyclerAdapter = PostRecyclerAdapter(postListesi)
            recyclerView.adapter = recyclerAdapter
            menuButton.setOnClickListener {
                bottomsheetfragment.show(supportFragmentManager,"BottomSheetDialog")
            }
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                    val visibleItemCount = layoutManager.childCount
                    val totalItemCount = layoutManager.itemCount
                    val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                    println("addOnlistenerçalıştı")
                    if (!isLastItemReached && visibleItemCount + firstVisibleItemPosition >= totalItemCount
                        && firstVisibleItemPosition >= 0
                    ) {
                        sonrakiSayfayiAl()
                    }
                }
            })
        }
        @SuppressLint("NotifyDataSetChanged")
        fun verileriAl(){
            database.collection("Post").orderBy("tarih",Query.Direction.DESCENDING).limit(20)
                .addSnapshotListener { value, error ->
                if (error!= null){
                    Toast.makeText(this,error.localizedMessage,Toast.LENGTH_LONG).show()
                }else{
                    if (value != null){
                        if (!value.isEmpty){
                            val documents = value.documents
                            postListesi.clear()
                            for (document in documents){
                                val kullaniciEmail = document.get("kullaniciemail") as String
                                val paylasim = document.get("paylasim") as String
                                val documentId = document.id
                                val nick = document.get("nick") as String
                                val indirilenPost = Post(kullaniciEmail,paylasim, documentId,nick)
                                postListesi.add(indirilenPost)
                            }
                            lastVisiblePost = documents[documents.size - 1]
                            recyclerAdapter.notifyDataSetChanged()
                        }
                    }
                }
            }
        }
        fun sonrakiSayfayiAl() {
            println("sonraki sayfayı al çalıştı")
            if (lastVisiblePost != null) {
                val query = database.collection("Post")
                    .orderBy("tarih", Query.Direction.DESCENDING)
                    .startAfter(lastVisiblePost!!)
                    .limit(20)
                query.get()
                    .addOnSuccessListener { documents ->
                        if (!documents.isEmpty) {
                            val newPosts = mutableListOf<Post>()
                            for (document in documents) {
                                val kullaniciEmail = document.get("kullaniciemail") as String
                                val paylasim = document.get("paylasim") as String
                                val documentId = document.id
                                val nick = document.get("nick") as String
                                val indirilenPost = Post(kullaniciEmail, paylasim, documentId,nick)
                                newPosts.add(indirilenPost)
                            }
                            lastVisiblePost = documents.documents[documents.size() - 1]
                            postListesi.addAll(newPosts)
                            recyclerAdapter.notifyDataSetChanged()
                        }
                    }
                    .addOnFailureListener { e ->
                        Log.e("Firestore", "Sonraki sayfa alınırken hata: $e")
                    }
            }
        }
    }