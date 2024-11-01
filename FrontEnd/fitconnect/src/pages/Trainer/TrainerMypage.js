import React, { useEffect, useRef, useState } from 'react';
import axios from 'axios';
import binder from 'classnames/bind'
import { Col, Container, Row, Card, Button, Form } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import TrainerMypage from './css/TrainerMypage.css'


const MyPage = () => {
  const [trainerInfo, setTrainerInfo] = useState({
    name:'',
    id: '',
    userName: '',
    email: '',
    regdate: '',
    profile: '',
    trainer_insta: '',
    trainer_intro: '',
    gym_name: '',
    gym_link: '',
  });
  // 프로필 이미지 src 에 적용할 값을 state 로 관리 하기
  const [imageSrc, setImageSrc] = useState(null);

  const personSvg = useRef();
  // 이미지 input 요소의 참조값을 사용하기 위해 
  const imageInput = useRef()

  const navigate = useNavigate();


  const profileStyle={
    width: "200px",
    height: "200px",
    border: "1px solid #cecece",
    borderRadius: "50%",
    justifyContent: "center"
  }
  const profileStyle2={
    width: "200px",
    height: "200px",
    border: "1px solid #cecece",
    borderRadius: "50%",
    display: "none",
    justifyContent: "center"    
  }

  // 본인정보를 가져오는 axios.get 요청
  useEffect(() => {
    axios.get(`/user`, {
      headers: {
        Authorization: localStorage.getItem('token')
      }
    })
    .then(res => {
      setTrainerInfo(prevInfo => ({
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

      axios.get(`/trainer`, {
        headers: {
          Authorization: localStorage.getItem('token')
        }
      })
      .then(res => { 
        setTrainerInfo(prevInfo => ({
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
      <Container className='TrainerMypage-container' style={{fontFamily:'nanumsquare', fontWeight:700}}>
      <Row>
      <Col>
         <Card>
          <Card.Header className="Header">
            마이페이지
          </Card.Header>
          <Card.Body className="">
          <svg ref={personSvg} style={profileStyle2}  xmlns="http://www.w3.org/2000/svg" width="16" height="16" fill="currentColor" viewBox="0 0 16 16">
            <path d="M11 6a3 3 0 1 1-6 0 3 3 0 0 1 6 0z"/>
            <path fillRule="evenodd" d="M0 8a8 8 0 1 1 16 0A8 8 0 0 1 0 8zm8-7a7 7 0 0 0-5.468 11.37C3.242 11.226 4.805 10 8 10s4.757 1.225 5.468 2.37A7 7 0 0 0 8 1z"/>
          </svg>
            
              <Row className='TrainerMypage-row'>
                <Col className='TrainerMypage-left'>
                <Form.Group>
                <Form.Label></Form.Label>
                    <Form.Control ref={imageInput} style={{display:"none"}} type="file" name="image" accept="image/*"/>
                    </Form.Group>
                  <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', textAlign: 'center' }}>
                  <div className="Trainerdrop-zone">

                        <img className="TrainerProfile-image" style={profileStyle} src={imageSrc} alt="프로필 이미지"/>

                  </div>
                  <p className='TrainerProfile-name' >{trainerInfo.name} 
                    <a href={trainerInfo.trainer_insta} target="_blank" rel="noopener noreferrer">
                    <img
                      src="/img/instagramlogo.png"
                      alt="Instagram Logo"
                      style={{ width: '40px', height: '40px' }} 
                    />
                      </a> </p>
                    <p>
                      {<a href={trainerInfo.gym_link} target="_blank" rel="noopener noreferrer" style={{textAlign:"center", fontWeight:550, textDecoration:"none", color:"#328DF4"}}>
                        {trainerInfo.gym_name}
                      </a>}</p>
                      </div>
                </Col>
                <Col className='TrainerMypage-right TrainerMypage-info'>
                  <p style={{marginBottom:40}}><b style={{fontSize:20}}>아이디</b> <p style={{opacity:0.9}}>{trainerInfo.userName}</p></p>
                  <p style={{marginBottom:40}}><b style={{fontSize:20}}>이메일</b> <p style={{opacity:0.9}}>{trainerInfo.email}</p></p>
                  <p style={{marginBottom:40}}><b style={{fontSize:20}}>생성일</b> <p style={{opacity:0.9}}>{trainerInfo.regdate}</p></p>
                  <p style={{marginBottom:40}}><b style={{fontSize:20}}>소갯글</b> <p style={{opacity:0.9}}>{trainerInfo.trainer_intro}</p></p> 
                </Col>
              </Row>
              <Button onClick={()=> navigate('/trainer/mypagedetail')}>회원정보수정</Button>
            </Card.Body>
          </Card>
        </Col>
      </Row>     
      </Container>
    </>
  );
};

export default MyPage;