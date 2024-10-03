//식단 조회 및 삭제
import axios from "axios"
import React, { useEffect, useState } from "react"
import { Button, Card, Row, Col } from "react-bootstrap"
import ProgressBar from 'react-bootstrap/ProgressBar'
import { useLocation, useNavigate, useParams } from "react-router-dom"

function MemberDietJournal(){

  const {member_num, m_calendar_id, d_journal_id} = useParams()

  const [formData,setFormData] = useState([])
  const [totalCarbs, setTotalCarbs] = useState(0)
  const [totalProtein, setTotalProtein] = useState(0)
  const [totalFat, setTotalFat] = useState(0)
  const [totalKcal, setTotalKcal] = useState(0)

  const token = localStorage.getItem('token')
  
  const navigate = useNavigate()
  const location = useLocation()
  const queryParams = new URLSearchParams(location.search)

  const today = new Date()
  const localDate = `${today.getFullYear()}-${String(today.getMonth() + 1).padStart(2, '0')}-${String(today.getDate()).padStart(2, '0')}`
  const initialDate = queryParams.get("date") ? queryParams.get("date") : localDate
  const [selectedDate, setSelectedDate] = useState(initialDate);

  useEffect(()=>{
    if(!queryParams.get("date"))
      navigate(`?date=${initialDate}`)
  },[initialDate, navigate, queryParams])

  useEffect(()=>{
    axios.get(`/dietjournal/${m_calendar_id}`)
    .then(res => {
      axios.get(`/membercalendar/${m_calendar_id}`)
      .then(calendarRes=>{
        const selectDateCalendar = calendarRes.data.date
        const filteredData = res.data.filter(item => selectDateCalendar === selectedDate)
        setFormData(filteredData)
    
        const carbsAll = filteredData.reduce((sum, data) => sum + data.carbs, 0)
        setTotalCarbs(carbsAll)
    
        const proteinAll = filteredData.reduce((sum, data) => sum + data.protein, 0)
        setTotalProtein(proteinAll)
    
        const fatAll = filteredData.reduce((sum, data) => sum + data.fat, 0)
        setTotalFat(fatAll)
    
        const kcalAll = filteredData.reduce((sum, data) => sum + data.calories, 0)
        setTotalKcal(kcalAll)
  
      })  
    })
    .catch(error => console.log(error))
  },[token, m_calendar_id,member_num,selectedDate])

  const getDietByType = (type) => {
    return formData.filter(data => data.diet_type === type)
  }

  const handleAllDelete=()=>{
    axios.delete(`/dietjournal/all/${m_calendar_id}`)
    .then(res=>{
      if(res.data.isSuccess){
        alert("삭제 완료되었습니다.")
        navigate('/MemberCalendar')
      }else{
        alert("식단 삭제 실패")
      }
    })
    .catch(error=>
      console.log(error),
      alert("일정삭제 에러")
    )
  }  

//이거 식단일지 삭제 => 흠 아래쪽에 따로
  const handleDelete=()=>{
    axios.delete(`/dietjournal/${d_journal_id}`)
    .then(res => {
      if(res.data.isSuccess){
        setFormData(data => data.filter(item => item.d_journal_id !== d_journal_id))
        alert("식단 삭제 완료되었습니다.")
      }else{
        alert("식단 삭제 실패")
      }
    })
    .catch(error=>console.log(error))
  }

  return (
    <>
    <div>
      <Row>
        <Col>
          <Card>
            <Card.Header as="h6" className="border-bottom p-3 mb-0"><div><h1>{selectedDate}의 식단</h1></div></Card.Header>
          </Card>
        </Col>
      </Row>
      <Row>
        <Col xs={24} md={12}>
            <Card>
            <Card.Header as="h6" className="border-bottom p-3 mb-0">
            <div className="d-flex justify-content-end mb-3">
              {/*이거 navigate 나중에 옮길땐 따로 바꿔야함*/}
              <Button onClick={()=>navigate('/MemberDietAdd')} variant="secondary" className="me-2">reserve</Button>
              <Button onClick={handleAllDelete} variant="secondary">Delete</Button>
            </div>
            </Card.Header>
            <Card.Body className="">
            <div>
              <Row className="align-items-center">
                <Col  xs={1}>
                <span>탄수화물</span>
                </Col>
                <Col>
                <ProgressBar striped variant="success" animated now={40} style={{height:'30px'}} />
                </Col>
                <Col xs="auto">
                <span>{totalCarbs}</span>
                </Col>
              </Row>
                <br/>
                <Row className="align-items-center">
                <Col  xs={1}>
                  <span>단백질</span>
                </Col>
                <Col>
                <ProgressBar striped variant="info" animated now={20} style={{height:'30px'}}/>
                </Col>
                <Col xs="auto">
                <span>{totalProtein}</span>
                </Col>
              </Row>
                <br/>
                <Row className="align-items-center">
                <Col xs={1}>
                  <span>지방</span>
                </Col>
                <Col>
                <ProgressBar striped variant="warning" animated now={60} style={{height:'30px'}}/>
                </Col>
                <Col xs="auto">
                  <span>{totalFat}</span>
                </Col>
              </Row>
                <br/>
                <Row className="align-items-center">
                <Col xs={1}>
                  <span>칼로리</span>
                </Col>
                <Col>
                <ProgressBar striped variant="danger" animated now={80} style={{height:'30px'}}/>
                </Col>
                <Col xs="auto">
                  <span>{totalKcal}</span>
                </Col>
              </Row>
            </div>
            </Card.Body>
          </Card>
        </Col>
      </Row>
     </div>
     <div >
      <Row className="g-3">
      {["아침", "점심", "저녁"].map((mealType) => (
            <Col xs={12} md={4} key={mealType}>
              <Card>
                <Card.Header as="h6" className="border-bottom p-3 mb-0 d-flex justify-content-between align-items-center">
                  {mealType}
                  <Button onClick={() => handleDelete()} variant="secondary">삭제</Button>
                </Card.Header>
                <Card.Body>
                  <ul>
                    {getDietByType(mealType).map((data) => (
                      <li key={data.d_journal_id}>
                        {data.food}: {data.foodCount}g 
                      </li>
                    ))}
                  </ul>
                </Card.Body>
              </Card>
            </Col>
          ))}
      </Row>
    </div>
     </>
  )
}

export default MemberDietJournal