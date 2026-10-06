import "./App.css"
import React from 'react';
import {
  BrowserRouter as Router,
  Route,
  Routes,
} from "react-router-dom";
import { AuthProvider } from "./Utils/AuthContext";
import Membership from './View/Membership';
import Consent from './View/Consent';
import Main from './View/Main'
import Login from "./View/Login";
function App() {
  return (
    <AuthProvider>
    <Router>
    <Routes>
    <Route path="/createuser" element={<Membership />} />
    <Route path="/consent" element={<Consent />} />
    <Route path="/" element={<Main />} />
    <Route path="/login" element={<Login />} />
    </Routes>
    </Router>
    </AuthProvider>
  );
}

export default App;
