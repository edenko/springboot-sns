/**
	2. 스토리 페이지
	(1) 스토리 로드하기
	(2) 스토리 스크롤 페이징하기
	(3) 좋아요, 안좋아요
	(4) 댓글쓰기
	(5) 댓글삭제
 */

// (1) 스토리 로드하기
let page = 0;
function storyLoad() {
	$.ajax({
		url: `/api/v1/image?page=${page}`,
		dataType: "json"
	}).done(res=>{
		res.data.content.forEach((u)=>{
			let item = getStoryItem(u);
			$("#storyList").append(item);
		});
	}).fail(err=>{
		console.log(err);
	});
}

storyLoad();

function getStoryItem(u) {
	let item = `<div class="story-list__item">
								<div class="sl__item__header">
									<div>
										<img class="profile-image" src="/upload/${u.user.profileImageUrl}"
											onerror="this.src='/images/person.jpeg'" />
									</div>
									<div>${u.user.username}</div>
								</div>
							
								<div class="sl__item__img">
									<img src="/upload/${u.postImageUrl}" />
								</div>
							
								<div class="sl__item__contents">
									<div class="sl__item__contents__icon">
							
										<button>`;

											if(u.likeState) {
												item += `<i class="fas fa-heart active" id="storyLikeIcon-${u.id}" onclick="toggleLike(${u.id})"></i>`;
											}else {
												item += `<i class="far fa-heart" id="storyLikeIcon-${u.id}" onclick="toggleLike(${u.id})"></i>`;
											}

										item += `
										</button>
									</div>
							
									<span class="like"><b id="storyLikeCount-${u.id}">${u.likeCount} </b>likes</span>`;

							u.caption != "" ?
								item += `<div class="sl__item__contents__content">
													 <p>${u.caption}</p>
												 </div>`
								: item += ``;

									item += `
									<div id="storyCommentList-${u.id}">
							
										<div class="sl__item__contents__comment" id="storyCommentItem-${u.id}">
											<p>
												<b>Lovely :</b> 부럽습니다.
											</p>
							
											<button>
												<i class="fas fa-times"></i>
											</button>
										</div>
							
									</div>
							
									<div class="sl__item__input">
										<input type="text" placeholder="댓글 달기..." id="storyCommentInput-${u.id}" />
										<button type="button" onClick="addComment()">게시</button>
									</div>
							
								</div>
							</div>`;
	return item;
}

// (2) 스토리 스크롤 페이징하기
// 문서의 높이 - 윈도우 높이 = 윈도우 스크롤탑
$(window).scroll(() => {
	// console.log($(window).scrollTop());
	// console.log($(document).height());
	// console.log($(window).height());
	let checkNum = $(window).scrollTop()-($(document).height()-$(window).height());
	// console.log(checkNum);
	if(checkNum < 1 && checkNum > -1) {
		page++;
		storyLoad();
	}
});


// (3) 좋아요, 안좋아요
function toggleLike(id) {
	let likeIcon = $(`#storyLikeIcon-${id}`);

	if (likeIcon.hasClass("far")) { // 좋아요
		$.ajax({
			type: "POST",
			url: `/api/v1/image/${id}/likes`,
			dataType: "json"
		}).done(res => {
			let likeCountStr = $(`#storyLikeCount-${id}`).text();
			let likeCount = Number(likeCountStr) + 1;
			$(`#storyLikeCount-${id}`).text(likeCount);

			likeIcon.addClass("fas");
			likeIcon.addClass("active");
			likeIcon.removeClass("far");
		}).fail(err => {
			console.log("오류", err);
		});
	} else { // 좋아요 취소
		$.ajax({
			type: "DELETE",
			url: `/api/v1/image/${id}/likes`,
			dataType: "json"
		}).done(res => {
			let likeCountStr = $(`#storyLikeCount-${id}`).text();
			let likeCount = Number(likeCountStr) - 1;
			$(`#storyLikeCount-${id}`).text(likeCount);

			likeIcon.removeClass("fas");
			likeIcon.removeClass("active");
			likeIcon.addClass("far");
		}).fail(err => {
			console.log("오류", err);
		});
	}
}

// (4) 댓글쓰기
function addComment() {

	let commentInput = $("#storyCommentInput-1");
	let commentList = $("#storyCommentList-1");

	let data = {
		content: commentInput.val()
	}

	if (data.content === "") {
		alert("댓글을 작성해주세요!");
		return;
	}

	let content = `
			  <div class="sl__item__contents__comment" id="storyCommentItem-2""> 
			    <p>
			      <b>GilDong :</b>
			      댓글 샘플입니다.
			    </p>
			    <button><i class="fas fa-times"></i></button>
			  </div>
	`;
	commentList.prepend(content);
	commentInput.val("");
}

// (5) 댓글 삭제
function deleteComment() {

}







