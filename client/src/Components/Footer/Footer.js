import "./Footer.css"
const Footer = () => {
    return(
    <footer>
    <div className="Footer">
    <div className="footer_copyright">
    <img src="assets/WalletStory_logo.png" alt="logo"/>
    <p className="footer_small"><small>WalletStory <span>|</span> © 2026. All rights reserved</small></p>
    </div>
    <div className="footer_info">
    <p>
    업체명: (주) 흐르온 <span>/</span> 대표자: 윤OO <br/>
    주소: 인천 남동구 oo빌딩 xxx호<br/>
    TEL. 032-123-4567 <span>/</span> 사업자번호: 123-4560-7890<br/>
    </p>
    </div>
    <div className="footer_meun">
    <li>공지사항</li>
    <li>1:1문의</li>
    </div>
    </div>
    </footer>
    )
}

export default Footer;