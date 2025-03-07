import axios from "axios";
import { useState } from "react";

const API_URL = "http://localhost:8080/api/meetings";
const [responseData, setResponseData] = useState("");

export const testBackend = async() => {
    try{
    const response = await axios.get(`${API_URL}/test`);
    setResponseData(response.data);
    } catch(error){
        console.error("Error fetching data: ", error);
        setResponseData("Error fetching data.");
    }
};
