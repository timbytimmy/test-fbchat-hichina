import axios from 'axios'

export const AXIOS = axios.create({
  baseURL: 'http://localhost:9052',
  withCredentials: true
})
