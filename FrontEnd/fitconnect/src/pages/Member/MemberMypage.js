import React, { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import { Col, Container, Row, Card, Button, Form } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';


const MyPage = () => {
  const [memberInfo, setMemberInfo] = useState({
    name: '',
    id: '',
    userName: '',
    email: '',
    profile: '',
    image: '',
    regdate: '',
    member_num: '',
    trainer_num: '',
    member_height: '',
    member_weight: '',
    member_gender: '',
    plan: '',
    weeklyplan: ''
  });
  // 프로필 이미지 src 에 적용할 값을 state 로 관리 하기
  const [imageSrc, setImageSrc] = useState(null);

  const personSvg = useRef();
  // 이미지 input 요소의 참조값을 사용하기 위해 
  const imageInput = useRef()

  const navigate = useNavigate();

  const dropZoneStyle={
    minHeight:"250px",
    minWidth:"250px",
    borderRadius:"10px",
    display:"flex",
    justifyContent:"center",
    alignItems:"center"
  }
  const profileStyle={
    maxWidth: "200px",
    height: "200px",
    border: "1px solid #cecece",
    borderRadius: "50%"
  }
  const profileStyle2={
    width: "200px",
    height: "200px",
    border: "1px solid #cecece",
    borderRadius: "50%",
    display: "none"
  }

  // 본인정보를 가져오는 axios.get 요청
  useEffect(() => {
    axios.get(`/user`, {
      headers: {
        Authorization: localStorage.getItem('token')
      }
    })
    .then(res => {
      setMemberInfo(prevInfo => ({
        ...prevInfo,
        ...res.data
      }));
      //만일 등록된 프로필 이미지가 있다면
      if(res.data.profile){
        setImageSrc(`http://52.78.38.12:8080/upload/${res.data.profile}`)
      }else{//없다면 
        // person svg 이미지를 읽어들여서 data url 로 만든다음 imageSrc 에 반영하기 
        // svg 이미지를 2 진 데이터 문자열로 읽어들여서 
        const svgString=new XMLSerializer().serializeToString(personSvg.current)
        // 2진데이터 문자열을 btoa (binary to ascii) 함수를 이용해서 ascii 코드로 변경
        const encodedData = btoa(svgString)
        // 변경된 ascii 코드를 이용해서 dataUrl 을 구성한다 
        const dataUrl = "data:image/svg+xml;base64," + encodedData;
        setImageSrc(dataUrl)
      }

      axios.get(`/member`, {
        headers: {
          Authorization: localStorage.getItem('token')
        }
      })
      .then(res => { 
        setMemberInfo(prevInfo => ({
          ...prevInfo,
          ...res.data
        }));
      })
      .catch(err => console.log(err));
    })
    .catch(err => console.log(err));
  }, []);

  

  return (
    <>
      <svg ref={personSvg} style={profileStyle2}  xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
        <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
        <path fillRule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"/>
      </svg>
      <Container>
      <Row>
      <Col>
         <Card sm={6} mb={6}>
          <Card.Header className="Header">
            마이페이지
          </Card.Header>
          <Card.Body className="">
            
              <Row>
                <Col sm={12} mb={12} lg={6}>
                  <Form.Group>
                    <Form.Control ref={imageInput} style={{display:"none"}} type="file" name="image" accept="image/*"/>
                  </Form.Group>
                  <div className="mb-3">
                    <div style={dropZoneStyle}>
                        <img style={profileStyle} src={imageSrc} alt="프로필 이미지"/>
                    </div>
                  </div>
                </Col>
                <Col sm={12} mb={12} lg={6} style={{fontFamily:'nanumsquare', fontWeight:700}}>
                    <p>이름: {memberInfo.name}</p>
                    <p>생성일: {memberInfo.regdate}</p>
                    <p>아이디: {memberInfo.userName}</p>
                    <p>이메일: {memberInfo.email}</p>
                    <p>키: {memberInfo.member_height}</p>
                    <p>몸무게: {memberInfo.member_weight}</p>
                    <p>성별: {memberInfo.member_gender}</p>
                    <p>최종 목표: {memberInfo.plan}</p>
                    <p>주간 목표: {memberInfo.weeklyplan}</p>
                </Col>
              </Row>
              <Button type="submit"  onClick={()=> navigate('/member/mypagedetail')}>회원정보수정</Button>     
            </Card.Body>
          </Card>
        </Col>
      </Row>     
      </Container>
    </>
  );
};

export default MyPage;
