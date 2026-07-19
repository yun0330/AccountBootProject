import "./Header.css"
import { Link } from "react-router-dom"
const Header = () => {
    return(
    <header>
    <div className="Header">
    <div className="top_logo">
    <Link to="/"><img src="assets/WalletStory_logo.png" alt="logo"/></Link>
    </div>
    <nav className="nav_bar">
    <div className="container">
    <ul className="gnb_wrap">
    <li><a href="#">홈</a></li>
    <li><a href="#feature">기능</a></li>
    <li><a href="#faq">FAQ</a></li>
    </ul>
    </div>
    <div className="info_menu">
    <Link><li>로그인</li></Link>
    <div className="menu_line"><li>|</li></div>
    <Link to="createuser"><li>회원가입</li></Link>
    </div>
    </nav>
    </div>
    </header>
    )
}

export default Header;