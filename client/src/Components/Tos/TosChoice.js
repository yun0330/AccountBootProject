
const TosChoice = () => {
    return(
    <div className="TosChoice">
    <div className="cho_title">
    <h1>개인정보 수집ㆍ이용 동의 (선택)</h1>
    <p>WalletStory(이하 "서비스")는 다음과 같은 목적으로 추가적인 개인정보를 수집ㆍ이용 합니다.<br/>
    해당 항목은 선택 동의 사항으로, 동의하지 않으셔도 서비스 기본 이용에는 제한이 없습니다.
    </p>
    </div>
    <div className="cho_category">
    <h2>수집하는 개인정보 항목</h2>
    <ul>
    <li>이메일 주소</li>
    <li>닉네임</li>
    <li>서비스 이용 기록 (관심 카테고리,예산 설정, 소비 패턴 등)</li>
    </ul>
    </div>
    <div className="cho_purpose">
    <h2>개인정보 수집 및 이용 목적</h2>
    <ul>
    <li>맟춤형 서비스 제공 (소비 패턴 분석)</li>
    <li>이벤트 및 혜택 안내(프로모션, 공지사항, 신규 기능 안내)</li>
    <li>서비스 이용 통계 및 분석을 통한 기능 개선</li>
    <li>개인 맞춤형 콘텐츠 및 알림 제공</li>
    </ul>
    </div>
    <div className="cho_period">
    <h2>보유 및 이용 기간</h2>
    <ul>
    <li>회원 탈퇴 시 또는 동의 철회 시까지 보관 및 이용</li>
    <li>단, 관련 법령에 따라 보관이 필요한 경우 해당 기간 동안 보관 후 파기</li>
    </ul>
    </div>
    <div className="cho_period">
    <h2>동의 거부 권리 안내</h2>
    <p>회원은 위 개인정보 수집ㆍ이용에 대해 동의를 거부할 권리가 있습니다. <br/>
    동의를 거부하셔도 서비스의 기본 기능(가계부 작성, 조회 등)은 이용할 수 있으나,<br/>
    맞춤형 서비스 및 이벤트 안내 등 일부 기능은 제한될 수 있습니다.
    </p>
    </div>
    </div>
    )
}

export default TosChoice;