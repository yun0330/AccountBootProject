import "./App.css"
import React from 'react';
import {
  BrowserRouter as Router,
  Route,
  Routes,
} from "react-router-dom";
import Membership from './View/Membership';
import Consent from './View/Consent';
import Main from './View/Main'
function App() {
  return (
    <Router>
    <Routes>
    <Route path="/createuser" element={<Membership />} />
    <Route path="/consent" element={<Consent />} />
    <Route path="/" element={<Main />} />
    </Routes>
    </Router>
    
  );
}

export default App;
