import { Button, Nav } from "react-bootstrap";
import { Link, useLocation } from "react-router-dom";
import user1 from "../assets/images/users/user4.jpg";
import probg from "../assets/images/bg/download.jpg";
import { FcAutomatic, FcCalendar, FcConferenceCall, FcGenericSortingDesc, FcHome, FcIphone, FcMenu, FcShop, FcSportsMode, FcViewDetails } from "react-icons/fc";
import { useRef } from "react";
import { AiOutlineCalendar, AiOutlineFileAdd, AiOutlineHome, AiOutlineMessage, AiOutlineShop, AiOutlineTeam, AiOutlineUser } from "react-icons/ai";
import { GiRunningShoe } from "react-icons/gi";
import { IoFastFoodOutline } from "react-icons/io5";

const Sidebar = () => {
  const showMobilemenu = () => {
    document.getElementById("sidebarArea").classList.toggle("showSidebar");
  };

  // 현재 경로 가져오기
  const location = useLocation();
  
  // 로그인에 포함되는 페이지를 알려주고, 로그인 페이지에 표시되면 안 되는 목록들을 검열
  const isAuthPage = location.pathname.startsWith("/login") || location.pathname.startsWith("/signup") || location.pathname.startsWith("/membersignup") || location.pathname.startsWith("/trainersignup");

  // 경로에 따라 다른 네비게이션 메뉴 설정
  let navigation = [];

  if (!isAuthPage && location.pathname.startsWith("/member") && localStorage.getItem("role") == "MEMBER") {
    // 멤버 관련 경로일 때 보여줄 메뉴 설정
    navigation = [
      { title: "메인 페이지", href: "/member", icon: <AiOutlineHome /> },
      { title: "캘린더", href: "/member/calendar", icon: <AiOutlineCalendar /> },
      { title: "식단 일지", href: "/member/dietjournal", icon: <IoFastFoodOutline />},
      { title: "식단 등록", href: "/member/dietadd", icon: <AiOutlineFileAdd /> },
      { title: "운동 일지", href: "/member/exercisejournal", icon: <GiRunningShoe /> },
      { title: "운동 등록", href: "/member/exerciseadd", icon: <AiOutlineFileAdd />},
      { title: "마이페이지", href: "/member/mypage", icon: <AiOutlineUser />},
    ];
  } else if (!isAuthPage && location.pathname.startsWith("/tr") && localStorage.getItem("role") == "TRAINER" ) {
    // 트레이너 관련 경로일 때 보여줄 메뉴 설정
    navigation = [
      { title: "메인 페이지", href: "/trainer", icon: <AiOutlineHome /> },
      { title: "회원목록", href: "/trainer/members", icon: <AiOutlineTeam />},
      { title: "캘린더", href: "/trainer/calendar", icon: <AiOutlineCalendar /> },
      { title: "메신저", href: "/trainer/message", icon: <AiOutlineMessage /> },
      { title: "마이페이지", href: "/trainer/mypage", icon: <AiOutlineUser /> },
    ];
  }

  const closeBtn=useRef()
/* 이 밑은 사이드의 프로필빼고는 건들일이.. */
  return (
    <div>
      <div className="d-flex align-items-center"></div>
      <div className="profilebg" >
        <div className="p-3 d-flex">
          
          <Button ref={closeBtn}
            variant="black"
            className="ms-auto d-lg-none"
            onClick={showMobilemenu}
          >
            <i className="bi bi-x"></i>
          </Button>
        </div>
        {/* 경로에 따라 동적 사용자 정보 표시 */}
        <div>
        </div>
      </div>
      <div className="p-3 mt-2">
        <Nav className="sidebarNav flex-column">
          {/* 네비게이션 메뉴 렌더링 */}
          {navigation.map((navi, index) => (
            <Nav.Item key={index} className="sidenav-bg">
              <Nav.Link
                onClick={()=>closeBtn.current.click()}
                as={Link}
                to={navi.href}
                className={
                  location.pathname === navi.href
                    ? "active nav-link py-3"
                    : "nav-link text-secondary py-3"
                }
              >
                {/* JSX 아이콘과 문자열 아이콘을 구분하여 렌더링 */}
                {typeof navi.icon === 'string' ? (
                  <i className={navi.icon}></i>
                ) : (
                  navi.icon
                )}
                <span className="ms-3 d-inline-block">{navi.title}</span>
              </Nav.Link>
            </Nav.Item>
          ))}
        </Nav>
      </div>
    </div>
  );
};

export default Sidebar;