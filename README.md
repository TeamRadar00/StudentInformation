# StudentInformation
학사정보



- [ ] 로그인(아이디, 비밀번호 찾기)
	ㄴ 등급(학생(재학,휴학), 교수)

메인
- [ ] 개인정보(소속, 학년, 최근 접속 시간, 비밀번호 번경)
- [ ] 공지사항//나중
- [ ] 교과/수강
- [ ] 성적/졸업



교과/수강
- [ ] 내강의(학생)
	ㄴ 학생 : 수강 신청한 강의들
- [ ] 수강신청 페이지(학생)
	ㄴ 수강 신청
	ㄴ 수강 철회
- [ ] 개설 강의 조회
	ㄴ 검색(학기+교수님이름, 학기+강좌번호,학기+강의이름) 
- [ ] 강의 개설/수정(교수님만 보이는걸로)

ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

성적/졸업
- [ ] 수강성적 조회(학생)
- [ ] 성적 이의 신청(학생)
	ㄴ 수강신청한 강의 목록들을 셀렉트 박스 형식으로 선택하게
	ㄴ 선택하면 글 적을 수 있음
- [ ] 성적 이의 신청 목록(교수님)
	ㄴ 셀렉트 박스(자기 강의 목록)
	ㄴ 이의  신청 리스트(신청 글 클릭하면 다음 페이지로 넘어감)
	ㄴ 페이지로 넘어가면 (셀렉트 박스(A+~F) 승인, 승인거부, 신청내용)
- [ ] 성적 입력/수정(교수님)
	ㄴ 셀렉트 박스(교수님 강의)
	ㄴ 특정 강의 수강중인 학생들 리스트+성적 입력 셀렉트 박스
- [ ] 졸업학점조회(학생)


ㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡㅡ

SpringBoot 2.7.2

Lombok, Thymeleaf, Spring Web, Spring Data JPA, Validation, MySQL Driver


<h3>User CRUD</h3>
<table>
<th>URL</th><th>요청</th><th>설명</th>

<tr>
<td>/members/login</td>
<td>GET</td>
<td>로그인 창 가져오기</td>
</tr>

<tr>
<td>/members/login</td>
<td>POST</td>
<td>로그인 창에서 로그인 버튼 누를 경우</td>
</tr>

<tr>
<td>/members/find-member</td>
<td>GET</td>
<td>아이디, 비밀번호 찾기 창 가져오기</td>
</tr>

<tr>
<td>/members/find-id</td>
<td>POST</td>
<td>아이디, 비밀번호 찾기 창에서 아이디 찾기 버튼 누를 경우</td>
</tr>

<tr>
<td>/members/find-password</td>
<td>POST</td>
<td>아이디, 비밀번호 찾기 창에서 비밀번호 찾기 버튼 누를 경우</td>
</tr>

<tr>
<td>/members/logout</td>
<td>GET</td>
<td>myInfo에서 로그아웃 버튼을 누를 경우</td>
</tr>

<tr>
<td>/members/password</td>
<td>GET</td>
<td>비밀번호 변경 창 가져오기</td>
</tr>

<tr>
<td>/members/password</td>
<td>POST</td>
<td>비밀번호 변경 창에서 변경 버튼을 누를 경우</td>
</tr>
</table>

<h3>Admin CRUD</h3>
<table>
<th>URL</th><th>요청</th><th>설명</th>

<tr>
<td>/admin</td>
<td>GET</td>
<td>어드민 전용 창 가져오기</td>
</tr>

<tr>
<td>/admin</td>
<td>POST</td>
<td>어드민 창에서 id에 해당하는 학생 정보 변경</td>
</tr>

<tr>
<td>/admin/register</td>
<td>POST</td>
<td>어드민 창에서 회원가입 버튼을 누를 경우</td>
</tr>
</table>

<h3>Lecture CRUD</h3>
<table>
<th>URL</th><th>요청</th><th>설명</th>

<tr>
<td>/lectures</td>
<td>GET</td>
<td>강의 개설 및 수정 창 가져오기(교수님 전용)</td>
</tr>

<tr>
<td>/lectures/new</td>
<td>POST</td>
<td>강의 개설</td>
</tr>

<tr>
<td>/lectures/{id}/edit</td>
<td>POST</td>
<td>강의 수정</td>
</tr>

<tr>
<td>/lectures/{id}/delete</td>
<td>POST</td>
<td>강의 삭제</td>
</tr>

<tr>
<td>/lectures/my</td>
<td>GET</td>
<td>내 강의 창 가져오기</td>
</tr>

<tr>
<td>/lectures/opened</td>
<td>GET</td>
<td>개설 강의 창 가져오기</td>
</tr>

<tr>
<td>/lectures/opened</td>
<td>POST</td>
<td>개설 강의 검색</td>
</tr>
</table>

<h3>Application CRUD</h3>
<table>
<th>URL</th><th>요청</th><th>설명</th>

<tr>
<td>/applications</td>
<td>GET</td>
<td>수강신청 창 가져오기</td>
</tr>

<tr>
<td>/applications</td>
<td>POST</td>
<td>선택한 강의 수강신청</td>
</tr>
</table>

<h3>Grade CRUD</h3>
<table>
<th>URL</th><th>요청</th><th>설명</th>

<tr>
<td>/grade/myGrade</td>
<td>GET</td>
<td>이번학기 수강 과목 성적, 전체 학점, 평균 학점 및 총 수강 학점 확인(학생 전용)</td>
</tr>

<tr>
<td>/grade/objection</td>
<td>GET</td>
<td>성적 이의신청 창 가져오기(학생 전용)</td>
</tr>

<tr>
<td>/grade/objection</td>
<td>POST</td>
<td>이의신청 전송(학생 전용)</td>
</tr>

<tr>
<td>/grade/readObjection/{gradeId}</td>
<td>GET</td>
<td>이의신청한 학생 글 가져오기(교수님 전용)</td>
</tr>

<tr>
<td>/grade/readObjection/{gradeId}</td>
<td>POST</td>
<td>이의신청 결과 반환(승인여부, 성적)</td>
</tr>

<tr>
<td>/grade/objectionList</td>
<td>GET</td>
<td>성적 이의신청 목록 창 가져오기(교수님 전용)</td>
</tr>
	
<tr>
<td>/grade/objectionList/{lectureId}</td>
<td>GET</td>
<td>자신이 개설한 강의를 선택하면 해당 강의 이의신청 목록 띄움(교수님 전용)</td>
</tr>
	
<tr>
<td>/grade/giveGrade</td>
<td>GET</td>
<td>강의 선택 후 강의 수강중인 학생 리스트 가져오기(교수님 전용)</td>
</tr>
	
<tr>
<td>/grade/giveGrade</td>
<td>POST</td>
<td>수강중인 학생에게 부여한 성적 반환(교수님 전용)</td>
</tr>
	
</table>

<iframe width="600" height="336" src="https://www.erdcloud.com/p/76xbxG7Thnh8F6YhK" frameborder="0" allowfullscreen></iframe>



