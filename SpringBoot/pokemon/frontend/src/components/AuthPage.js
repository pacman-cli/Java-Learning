import React, { useState } from "react";
import LoginForm from "./LoginForm";
import SignupForm from "./SignupForm";

const AuthPage = ({ onLogin }) => {
  const [isLogin, setIsLogin] = useState(true);

  const switchToLogin = () => setIsLogin(true);
  const switchToSignup = () => setIsLogin(false);

  return (
    <div>
      {isLogin ? (
        <LoginForm onLogin={onLogin} switchToSignup={switchToSignup} />
      ) : (
        <SignupForm switchToLogin={switchToLogin} />
      )}
    </div>
  );
};

export default AuthPage;
