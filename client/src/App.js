import React from 'react';
import {
  BrowserRouter as Router,
  Route,
  Routes,
} from "react-router-dom";
import Membership from './View/Membership';
function App() {
  return (
    <Router>
      <Routes>
    <Route path="/createuser" element={<Membership />} />
    </Routes>
    </Router>
    
  );
}

export default App;
