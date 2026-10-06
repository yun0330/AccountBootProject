
const TosCollection = () => {
    return(
    <div className="Collection">
    <div className="etc_title">
    <h1>개인정보 수집ㆍ이용 동의서 (필수)</h1>
    </div>
    <div className="etc_category">
    <h2>수집하는 개인정보 항목</h2>
    <ol>
    <li>자체 회원가입 시</li>
        <ul>
        <li>필요항목: 이름, 아이디, 비밀번호, 닉네임</li>
        <li>선택항목: 프로필 사진, 연락처</li>
    </ul>
    <li>카카오 소셜 로그인 이용 시</li>
        <ul>
        <li>필수항목: 구글 계정 식별자(ID), 닉네임</li>
        <li>선택항목: 이메일(제공 동의 시), 프로필 사직, 연락처</li>
        </ul>
    <li>서비스 이용 과정에서 자동으로 생성 및 수집되는 정보</li>
        <ul>
        <li>수입ㆍ지출 기록, 카테고리 및 예산 설정 등 가계부 관련 데이터</li>
        <li>접속 로그, IP 주소, 쿠키, 기기 정보, 이용 기록</li>
        </ul>
    <li>고객 문의/1:1 상담 시</li>
        <ul>
        <li>이메일 주소, 문의 내용</li>
        </ul>
    </ol>
    </div>
    <div className="etc_purpose">
    <h2>개인정보의 수집 및 이용 목적</h2>
    <ul>
    <li>회원 식별 및 로그인 서비스 제공</li>
    <li>가계부 기능 제공 (수입ㆍ지출 관리, 예산 설정, 통계 제공)</li>
    <li>서비스 개선, 신규 기능 개발, 맟춤형 서비스 제공</li>
    <li>불법ㆍ부정 이용방지 및 안전한 서비스 운영</li>
    <li>고객 문의 및 민원 처리</li>
    </ul>
    </div>
    <div className="etc_period">
    <h2>개인정보의 보유 및 이용 기간</h2>
    <p>회원 탈퇴 시 즉시 파기</p>
    <ul>
    <li>단, 관련 법령(전자상거래법, 통신비밀보호법 등)에 따라 일정기간 보관이 필요한 경우 해당 법령에 따름</li>
        <ul>
            <li>계약/청약철회 기록: 5년</li>
            <li>소비자 불만 및 분쟁 처리 기록: 3년</li>
            <li>접속 로그/IP 기록: 3개월</li>
        </ul>
    </ul>
    </div>
    <div className="etc_refusal">
    <h2>동의 거부 권리 및 불이익 안내</h2>
    <p>회원은 개인정보 수집 및 이용에 대해 동의를 거부할 수 있습니다.<br/>
    단,  필수 항목에 대한 동의를 거부할 경우 회원가입 또는 서비스 이용이 제한될 수 있습니다.
    </p>
    </div>
    </div>
    )
}

export default TosCollection;