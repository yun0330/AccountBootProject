import "./Main.css"
import { RiBookShelfLine,RiCalculatorLine,
RiDoorLockBoxLine,RiSmartphoneLine} from "react-icons/ri";
import { FaChartPie,FaChartLine,
FaFileImport,FaRegPenToSquare} from "react-icons/fa6";
import Header from "../Components/Header/Header";

const Main = () => {
    return(
    <div>
    <Header/>
    <main className="Main">
    <section className="main_section">
    <div className="main_logo">
    <img src="assets/WalletStory_main.png" alt="main"/>
    </div>
    </section>
    <section className="main_section">
    <div className="main_function">
    <div className="function_wrap">
    <p className="function_icon"><RiBookShelfLine /></p>
    <h3>쉬운 복식부기</h3>
    <p className="function_text">꼼꼼하고 깔끔하게 관리할 수 있습니다.</p>
    </div>
    <div className="function_wrap">
    <p className="function_icon"><RiCalculatorLine /></p>
    <h3>정리에서 오는 안정감</h3>
    <p className="function_text">수입과 지출은 계속해서 이루어집니다. 정확히 파악하고 미래 계획을
    세울수 있게 도와줍니다.
    </p>
    </div>
    <div className="function_wrap">
    <p className="function_icon"><RiDoorLockBoxLine /></p>
    <h3>안전한 데이터</h3>
    <p className="function_text">모든 데이터는 철저하게 관리 되며, 주요한 개인정보는 암호화되어 저장됩니다.</p>
    </div>
    <div className="function_wrap">
    <p className="function_icon"><RiSmartphoneLine /></p>
    <h3>뛰어난 접근성</h3>
    <p className="function_text">웹/모바일앱에서도 관리가 가능합니다.</p>
    </div>
    </div>
    </section>
    <section className="main_section" id="feature">
    <div className="main_feature">
    <div className="feature_title">
    <h1>주요특징</h1>
    </div>
    <div className="feature_wrap">
    <p className="feature_icon"><FaRegPenToSquare /></p>
    <h3>간편한 입력</h3>
    <p className="feature_text">카테고리,금액,메모까지 한번에!</p>
    </div>
    <div className="feature_wrap">
    <p className="feature_icon"><FaChartPie /></p>
    <h3>예산 관리</h3>
    <p className="feature_text">예산을 설정하고 사용 비율을 한눈에 확인!</p>
    </div>
    <div className="feature_wrap">
    <p className="feature_icon"><FaChartLine /></p>
    <h3>지출 통계</h3>
    <p className="feature_text">지출 내역을 그래프로 한눈에!</p>
    </div>
    <div className="feature_wrap">
    <p className="feature_icon"><FaFileImport /></p>
    <h3>엑셀 연동</h3>
    <p className="feature_text">한달 수입·지출 내용을 엑셀로!</p>
    </div>
    </div>
    </section>
    <section className="main_section" id="faq">
    <div className="main_faq">
    <div className="faq_title">
    <h1>자주 묻는 질문</h1>
    </div>
    <details className="faq_details">
    <summary className="faq_summary">무료로 사용할 수 있나요?</summary>
    <div className="faq_text">
    저희 WalletStory는 무료로 제공 합니다, 부담 없이 사용해보세요.
    </div>
    </details>
    <details className="faq_details">
    <summary className="faq_summary">데이터는 어디에 저장되나요?</summary>
    <div className="faq_text">
    중요 정보는 암호화해 저장되고, 안전하게 서버에 보관됩니다.
    </div>
    </details>
    </div>
    </section>
    </main>
    </div>
    )
}

export default Main;