$(document).ready(function(){
	$.ajax({
        url: "/posts"
    }).then(function(data) {
    	$.each(data, function(index, e) {
    		$('#posts').append(
    				'<div class="card mb-4"> <div class="card-body"> <h2 class="card-title">' + e.title 
    				+ '</h2> <p class="card-text">' + e.content 
    				+ '</p> <a href="/page/detail/' + e.id 
    				+ '" class="btn btn-primary">Read More &rarr;</a> </div> ' 
    				+ '<div class="card-footer text-muted"> Posted on ' + e.updtDate 
    				+ ' by ' + e.user 
    				+ '</div> </div>');
    	});
       console.log(data);
    }, function(err) {
    	console.log(err.responseJSON);
    });
	
	$('#save_post_btn').click(function(){
        var user = $('#create_user_text').val();
        var title = $('#create_title_text').val();
        var content = $('#create_content_text').val();
        
        console.log(user);
        console.log(title);
        console.log(content);
        
        var param = {
            user: user,
            title: title,
            content: content
        }
        
        $.ajax({
            url: "/post",
            method: "POST",
            dataType: 'json',
            contentType: 'application/json',
            data: JSON.stringify(param)
        }).then(function(data) {
            // CEK BALASAN DARI BACKEND
            if (data.result === 200) {
                // Jika sukses simpan, kembali ke halaman utama
                window.location.href = '/';
            } else {
                // Jika gagal (input kosong), tampilkan pesan error dari Java
                alert(data.message);
            }
        }, function(err) {
            // Jika server mati atau error 500
            alert("Terjadi kesalahan koneksi ke server.");
        });
    });
});