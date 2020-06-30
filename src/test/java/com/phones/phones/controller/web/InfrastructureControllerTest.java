package com.phones.phones.controller.web;

//@PrepareForTest(RestUtils.class)
//@RunWith(PowerMockRunner.class)
public class InfrastructureControllerTest {

      /*  InfrastructureController infrastructureController;

        @Mock
        String token = "infrastructure1";

        @Mock
        CallService callService;


        @Before
        public void setUp() {
            initMocks(this);
            PowerMockito.mockStatic(RestUtils.class);
            infrastructureController = new InfrastructureController(callService );
        }


        @Test
        public void createCallOk() throws LineCannotMakeCallsException, LineNumberDoesNotExistException, UserInvalidLoginException {
            InfrastructureCallDto infrastructureCallDto = TestFixture.testInfrastructureCallDto();
            Call call = TestFixture.testCall();

            when(callService.create(infrastructureCallDto)).thenReturn(call);
            when(RestUtils.getLocation(call.getId())).thenReturn(URI.create("miUri.com"));

            ResponseEntity response = infrastructureController.createCall("infrastructure1", infrastructureCallDto);

            assertEquals(URI.create("miUri.com"), response.getHeaders().getLocation());
            assertEquals(HttpStatus.CREATED, response.getStatusCode());
        }*/
}
