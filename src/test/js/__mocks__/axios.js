


const mockAxiosInstance = jest.genMockFromModule("axios")

mockAxiosInstance.create = jest.fn( () => mockAxiosInstance);
mockAxiosInstance.get = jest.fn( () => Promise.resolve({ data:{} }))
mockAxiosInstance.put = jest.fn( () => Promise.resolve({ data:{} }))


export default mockAxiosInstance