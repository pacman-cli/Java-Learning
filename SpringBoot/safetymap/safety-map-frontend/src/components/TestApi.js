import React, { useEffect } from "react";
import { getAllPlaces } from "api/placeApi";

const TestApi = () => {
  useEffect(() => {
    getAllPlaces().then((res) => console.log(res.data));
  }, []);

  return <div>Check console for API data</div>;
};

export default TestApi;
