import React, { useState, useEffect } from 'react';
import axios from 'axios';
import './OutlookConnector.css'; // You'll need to create this CSS file

const OutlookConnector = () => {
  const [isConnected, setIsConnected] = useState(false);
  const [isLoading, setIsLoading] = useState(true);
  const [availableTimes, setAvailableTimes] = useState([]);
  const [dateRange, setDateRange] = useState({
    start: new Date(),
    end: new Date(new Date().setDate(new Date().getDate() + 7)) // Default to next 7 days
  });
  const [error, setError] = useState(null);

  // Check if calendar is already connected on component mount
  useEffect(() => {
    const checkConnectionStatus = async () => {
      try {
        const response = await axios.get('/api/calendar/status');
        setIsConnected(response.data.connected);
        if (response.data.connected) {
          fetchAvailableTimes();
        }
        setIsLoading(false);
      } catch (err) {
        console.error('Error checking connection status:', err);
        setError('Error checking calendar connection. Please try again later.');
        setIsLoading(false);
      }
    };

    checkConnectionStatus();
  }, []);

  // Format date for display
  const formatDate = (date) => {
    return new Intl.DateTimeFormat('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
      hour: 'numeric',
      minute: 'numeric'
    }).format(new Date(date));
  };

  // Format duration for display
  const formatDuration = (startTime, endTime) => {
    const start = new Date(startTime);
    const end = new Date(endTime);
    const durationMinutes = (end - start) / (1000 * 60);
    
    if (durationMinutes < 60) {
      return `${durationMinutes} min`;
    } else {
      const hours = Math.floor(durationMinutes / 60);
      const minutes = durationMinutes % 60;
      return minutes === 0 ? `${hours} hr` : `${hours} hr ${minutes} min`;
    }
  };

  // Handle connect button click
  const handleConnectClick = async () => {
    try {
      setIsLoading(true);
      const response = await axios.get('/api/calendar/connect/outlook');
      
      // Redirect to Microsoft login
      window.location.href = response.data.authUrl;
    } catch (err) {
      console.error('Error initiating connection:', err);
      setError('Error connecting to Outlook. Please try again later.');
      setIsLoading(false);
    }
  };

  // Handle disconnect button click
  const handleDisconnectClick = async () => {
    try {
      setIsLoading(true);
      await axios.post('/api/calendar/disconnect');
      setIsConnected(false);
      setAvailableTimes([]);
      setIsLoading(false);
    } catch (err) {
      console.error('Error disconnecting calendar:', err);
      setError('Error disconnecting calendar. Please try again later.');
      setIsLoading(false);
    }
  };

  // Fetch available times from the connected calendar
  const fetchAvailableTimes = async () => {
    try {
      setIsLoading(true);
      
      // Format dates for API
      const startParam = dateRange.start.toISOString();
      const endParam = dateRange.end.toISOString();
      
      const response = await axios.get('/api/calendar/available', {
        params: {
          start: startParam,
          end: endParam
        }
      });
      
      setAvailableTimes(response.data.availableSlots || []);
      setIsLoading(false);
    } catch (err) {
      console.error('Error fetching available times:', err);
      setError('Error retrieving your availability. Please try again later.');
      setIsLoading(false);
    }
  };

  // Update date range and fetch new available times
  const handleDateRangeChange = (type, event) => {
    const newDate = new Date(event.target.value);
    setDateRange(prev => ({
      ...prev,
      [type]: newDate
    }));
  };

  return (
    <div className="outlook-connector">
      <div className="connector-header">
        <h2>Connect your Outlook Calendar</h2>
        <p className="description">
          MeetMerge uses your calendar to find available meeting times. 
          We only access your free/busy information, not the details of your events.
        </p>
      </div>
      
      {error && (
        <div className="error-message">
          {error}
          <button className="dismiss-button" onClick={() => setError(null)}>
            Dismiss
          </button>
        </div>
      )}
      
      <div className="connection-status">
        {isLoading ? (
          <div className="loading">
            <div className="spinner"></div>
            <span>Loading...</span>
          </div>
        ) : isConnected ? (
          <div className="connected-status">
            <div className="status-indicator connected"></div>
            <span>Your Outlook calendar is connected</span>
            <button 
              className="disconnect-button" 
              onClick={handleDisconnectClick}
              disabled={isLoading}
            >
              Disconnect
            </button>
          </div>
        ) : (
          <div className="connect-container">
            <button 
              className="connect-button" 
              onClick={handleConnectClick}
              disabled={isLoading}
            >
              <svg className="outlook-icon" viewBox="0 0 24 24" width="24" height="24">
                <path d="M21.1789 4.4344H10.7769V12.4004L14.1249 13.6324V7.7624H20.0969V16.4364H14.1249V15.0084L10.7769 16.2544V19.5944H21.5529C22.3559 19.5944 22.9999 18.9504 22.9999 18.1474V5.8814C22.9999 5.0784 22.3559 4.4344 21.5529 4.4344H21.1789Z" fill="#0078D4"/>
                <path d="M1 12.0001C1 10.1241 2.176 8.5001 3.878 7.7541C5.218 7.1581 6.784 7.1661 8.122 7.7701C9.824 8.5161 11 10.1401 11 12.0161C11 13.8921 9.824 15.5161 8.122 16.2621C6.784 16.8661 5.218 16.8741 3.878 16.2781C2.176 15.5321 1 13.8761 1 12.0001Z" fill="#0078D4"/>
              </svg>
              Connect Outlook Calendar
            </button>
            <p className="privacy-note">
              <strong>Privacy:</strong> We only see when you're available, not what you're doing.
            </p>
          </div>
        )}
      </div>
      
      {isConnected && (
        <div className="available-times-section">
          <div className="date-range-selector">
            <h3>Check Your Availability</h3>
            <div className="date-inputs">
              <div className="date-input-group">
                <label htmlFor="start-date">From</label>
                <input 
                  type="datetime-local" 
                  id="start-date"
                  value={dateRange.start.toISOString().substring(0,16)}
                  onChange={(e) => handleDateRangeChange('start', e)}
                />
              </div>
              <div className="date-input-group">
                <label htmlFor="end-date">To</label>
                <input 
                  type="datetime-local" 
                  id="end-date"
                  value={dateRange.end.toISOString().substring(0,16)}
                  onChange={(e) => handleDateRangeChange('end', e)}
                />
              </div>
              <button 
                className="update-button"
                onClick={fetchAvailableTimes}
                disabled={isLoading}
              >
                Update
              </button>
            </div>
          </div>
          
          <div className="times-container">
            <h3>Your Available Times</h3>
            {isLoading ? (
              <div className="loading">
                <div className="spinner"></div>
                <span>Loading your availability...</span>
              </div>
            ) : availableTimes.length > 0 ? (
              <ul className="available-times-list">
                {availableTimes.map((slot, index) => (
                  <li key={index} className="time-slot">
                    <div className="time-range">
                      <div className="date-display">
                        {formatDate(slot.start)} to {formatDate(slot.end)}
                      </div>
                      <div className="duration-display">
                        {formatDuration(slot.start, slot.end)}
                      </div>
                    </div>
                  </li>
                ))}
              </ul>
            ) : (
              <p className="no-times-message">
                No available times found in the selected range.
              </p>
            )}
          </div>
        </div>
      )}
    </div>
  );
};

export default OutlookConnector;
