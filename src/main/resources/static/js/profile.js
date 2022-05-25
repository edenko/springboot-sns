/**
  1. 유저 프로파일 페이지
  (1) 유저 프로파일 페이지 구독하기, 구독취소
  (2) 구독자 정보 모달 보기
  (3) 구독자 정보 모달에서 구독하기, 구독취소
  (4) 유저 프로필 사진 변경
  (5) 사용자 정보 메뉴 열기 닫기
  (6) 사용자 정보(회원정보, 로그아웃, 닫기) 모달
  (7) 사용자 프로파일 이미지 메뉴(사진업로드, 취소) 모달 
  (8) 구독자 정보 모달 닫기
 */

// (1) 유저 프로파일 페이지 구독하기, 구독취소
function toggleSubscribe(toUserId, obj) {
	if ($(obj).text() === "구독취소") {
		$.ajax({
			type: "post",
			url: "/api/v1/unSubscribe/" + toUserId,
			dataType: "json"
		}).done(res=>{
			$(obj).text("구독하기");
			$(obj).toggleClass("blue");
		}).fail(error=>{
			console.log("구독취소 실패", error);
		});
	} else {
		$.ajax({
			type: "post",
			url: "/api/v1/subscribe/" + toUserId,
			dataType: "json"
		}).done(res=>{
			$(obj).text("구독취소");
			$(obj).toggleClass("blue");
		}).fail(error=>{
			console.log("구독하기 실패", error);
		});
	}
}

// (2) 구독자 정보  모달 보기
function subscribeInfoModalOpen(pageUserId) {
	$(".modal-subscribe").css("display", "flex");

	$.ajax({
		url: `/api/v1/user/${pageUserId}/subscribe`,
		dataType: "json"
	}).done(res=>{
		res.data.forEach((u)=>{
			let item = getSubscribeModalItem(u);
			$("#subscribeModalList").append(item);
		});
	}).fail(error=>{
		console.log("구독 정보 불러오기 실패", error);
	});
}

function getSubscribeModalItem(u) {
	let item = `<div class="subscribe__item" id="subscribeModalItem-${u.id}">
							\t<div class="subscribe__img">
							\t\t<img src="/upload/${u.profileImageUrl}" onerror="this.src='/images/person.jpeg'"/>
							\t</div>
							\t<div class="subscribe__text">
							\t\t<h2>${u.username}</h2>
							\t</div>
							\t<div class="subscribe__btn">`;

							if(!u.equalUserState) {
								if(u.subscribeState) {
									item += `\t\t<button class="cta blue" onClick="toggleSubscribe(${u.id}, this)">구독취소</button>`;
								}else {
									item += `\t\t<button class="cta" onClick="toggleSubscribe(${u.id}, this)">구독하기</button>`;
								}
							}

							item += `
							\t</div>
							</div>`;
	return item;
}


// (3) 구독자 정보 모달에서 구독하기, 구독취소 -> 필요없어요
// function toggleSubscribeModal(obj) {
// 	if ($(obj).text() === "구독취소") {
// 		$(obj).text("구독하기");
// 		$(obj).toggleClass("blue");
// 	} else {
// 		$(obj).text("구독취소");
// 		$(obj).toggleClass("blue");
// 	}
// }

// (4) 유저 프로파일 사진 변경 (완)
function profileImageUpload(pageUserId, principalId) {
	if(pageUserId != principalId) {
		return alert("수정할 수 있는 권한이 없습니다.");
	}

	$("#userProfileImageInput").click();

	$("#userProfileImageInput").on("change", (e) => {
		let f = e.target.files[0];

		if (!f.type.match("image.*")) {
			alert("이미지를 등록해야 합니다.");
			return;
		}

		// 서버에 이미지 전송
		let profileImageForm = $('#userProfileImageForm')[0];
		// 1. formData 객체를 이용하면 form 태그의 필드와 그 값을 나타내는 일련의 key/value 쌍을 담을 수 있음
		let formDate = new FormData(profileImageForm); // form 태그 자체를 넣으면 값만 담김
		// 2. ajax 호출
		$.ajax({
			type: "put",
			url: `/api/v1/user/${principalId}/profileImageUrl`,
			data: formDate,
			contentType: false, // 필수 : x-www-form-urlencoded로의 파싱 방지
			processData: false, // QueryString 자동 설정 false
			enctype: "multipart/form-data",
			dataType: "json"
		}).done(res => {
			// 사진 전송 성공시 이미지 변경
			let reader = new FileReader();
			reader.onload = (e) => {
				$("#userProfileImage").attr("src", e.target.result);
			}
			reader.readAsDataURL(f); // 이 코드 실행시 reader.onload 실행됨.
		}).fail(err => {
			console.log(`오류 : ${err}`);
		});
	});
}


// (5) 사용자 정보 메뉴 열기 닫기
function popup(obj) {
	$(obj).css("display", "flex");
}

function closePopup(obj) {
	$(obj).css("display", "none");
}


// (6) 사용자 정보(회원정보, 로그아웃, 닫기) 모달
function modalInfo() {
	$(".modal-info").css("display", "none");
}

// (7) 사용자 프로파일 이미지 메뉴(사진업로드, 취소) 모달
function modalImage() {
	$(".modal-image").css("display", "none");
}

// (8) 구독자 정보 모달 닫기
function modalClose() {
	$(".modal-subscribe").css("display", "none");
	location.reload();
}






