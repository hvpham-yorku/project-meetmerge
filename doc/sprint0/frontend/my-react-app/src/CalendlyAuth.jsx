import { useState, useEffect } from "react";
import axios from "axios";

const CalendlyAuth = () => {
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [userInfo, setUserInfo] = useState(null);
  const [error, setError] = useState("");
  const [clientId, setClientId] = useState("");
  const [isLoading, setIsLoading] = useState(true);
  const [busyTimes, setBusyTimes] = useState([]);

  const REDIRECT_URI = "http://localhost:5173/"; 

  useEffect(() => {
    const fetchOAuthConfig = async () => {
      try {
        const response = await axios.get("http://localhost:8080/api/calendly/oauth-config");
        if (response.data && response.data.clientId) {
          setClientId(response.data.clientId);
        } else {
          setError("Failed to load OAuth configuration");
        }
      } catch (error) {
        console.error("Error fetching OAuth config:", error);
        setError("Failed to load OAuth configuration");
      } finally {
        setIsLoading(false);
      }
    };

    fetchOAuthConfig();
    
  }, []);

  useEffect(() => {
    if(!isLoading){
      // Check if we're returning from Calendly auth
    const urlParams = new URLSearchParams(window.location.search);
    const code = urlParams.get("code");
    
    if (code) {
      // Exchange the code for an access token
      exchangeCodeForToken(code);
    }
    }
    
  }, [isLoading]);

  const handleLogin = () => {
    if(!clientId){
      setError("Client ID not available");
      return;
    }

     // Redirect to Calendly for authorization
     const authUrl = `https://auth.calendly.com/oauth/authorize?client_id=${clientId}&response_type=code&redirect_uri=${encodeURIComponent(REDIRECT_URI)}`;
     window.location.href = authUrl;
  };


  const exchangeCodeForToken = async (code) => {
    try {
      // Clear the URL parameters to avoid issues on page refresh
      window.history.replaceState({}, document.title, window.location.pathname);
      
      // This exchange should happen on your backend for security
      const response = await axios.post("http://localhost:8080/api/calendly/exchange-token", {
        code: code,
        redirect_uri: REDIRECT_URI,
      });
      
      if (response.data && response.data.access_token) {
        localStorage.setItem("calendly_access_token", response.data.access_token);
        setIsAuthenticated(true);
        fetchUserInfo(response.data.access_token);
        fetchBusyTimesList(response.data.access_token); 
      }
    } catch (error) {
      console.error("Token exchange error:", error);
      setError("Failed to authenticate with Calendly");
    }
  };

  const fetchUserInfo = async (token) => {
    try {
      const response = await axios.get("http://localhost:8080/api/calendly/user-info", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      });
      
      setUserInfo(response.data);
    } catch (error) {
      console.error("Error fetching user info:", error);
      setError("Failed to get user information");
    }
   
  };

  const handleLogout = () => {
    localStorage.removeItem("calendly_access_token");
    setIsAuthenticated(false);
    setUserInfo(null);
  };

  const fetchBusyTimesList = async (token) => {
    try {
      const response = await axios.get("http://localhost:8080/api/calendly/busy-times", {
        headers: { Authorization: `Bearer ${token}` },
        params: { user: clientId },
      });
      
      if (response.data) {
        setBusyTimes(response.data);  
      }

    } catch(error){
      console.error("Error fetching busy times:", error);
    }
  };

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
    <div className="p-4 max-w-md mx-auto">
      <h2 className="text-xl font-bold mb-4">Calendly Integration</h2>
      
      {error && <div className="bg-red-100 p-3 rounded mb-4 text-red-700">{error}</div>}
      
      {!isAuthenticated ? (
        <button
          onClick={handleLogin}
          className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 flex items-center"
          disabled={!clientId}
        >
          <svg 
            className="w-5 h-5 mr-2" 
            viewBox="0 0 24 24"
            fill="currentColor"
          >
            <path d="M12 2C6.48 2 2 6.48 2 12s4.48 10 10 10 10-4.48 10-10S17.52 2 12 2zm0 18c-4.41 0-8-3.59-8-8s3.59-8 8-8 8 3.59 8 8-3.59 8-8 8zm-1-13h2v6h-2zm0 8h2v2h-2z" />
          </svg>
          Login with Calendly
        </button>
      ) : (
        <div className="border rounded p-4">
          <div className="mb-4">
            {userInfo && (
              <>
                <p className="font-medium">Logged in as: {userInfo.name}</p>
                <p className="text-sm text-gray-600">{userInfo.email}</p>
                <p className="text-sm text-gray-600">{userInfo.scheduling_url}</p>
                <ul>
                  {Array.isArray(busyTimes) && busyTimes.length > 0 ? (
                    busyTimes.map((time, index) => (
                      <li key={index}>
                        {new Date(time.start_time).toLocaleString()} - {new Date(time.end_time).toLocaleString()}
                      </li>
                    ))
                  ) : (
                    <li>No busy times available</li>
                  )}
                </ul>
              </>
            )}
          </div>
          
          <button
            onClick={handleLogout}
            className="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600"
          >
            Logout
          </button>
        </div>
      )}
    </div>
  );
};

export default CalendlyAuth;
