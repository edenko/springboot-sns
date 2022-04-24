// (1) 회원정보 수정
function update(userId, event) {
  event.preventDefault() // form 태그 액션을 막음
  let data = $('#profileUpdate').serialize();

  $.ajax({
    type: "PUT",
    url: `/api/v1/user/${userId}`,
    data: data,
    contentType: "application/x-www-form-urlencoded; charset=utf-8",
    dataType: "json"
  }).done(res=>{ // 코드 200번대
    // console.log("success", res);
    location.href=`/user/${userId}`;
  }).fail(error=>{ // 코드 200번대 외
    if (error.responseJSON.data == null) {
      alert(error.responseJSON.message);
    }else {
      alert(JSON.stringify(error.responseJSON.data.name));
    }
    // console.log("fail", error.responseJSON);
  });
}