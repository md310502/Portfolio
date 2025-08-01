// ---
// C# TCP and UDP coursework interface server for the development and testing of socket applications.
// Author: Dr Ethan Bayne
// Built: 2022-02-23
// Built with aid from Microsoft .NET Sockets documentation: https://docs.microsoft.com/en-us/dotnet/api/system.net.sockets?view=netframework-4.5
// ---

using System;
using System.Data;
using System.Drawing;
using System.Text;
using System.Windows.Forms;
using System.Net;
using System.Net.Sockets;
using System.Threading;
using System.Text.RegularExpressions;

namespace CMP109SocketServer
{
    public partial class MainInterface : Form
    {
        // Global variables for service status and threads.
        public Boolean tcpServerStatus = false;
        public Boolean udpServerStatus = false;
        Thread tcpThread;
        Thread udpThread;

        // Global variables for server's services, for shutting down cleanly from main thread if required.
        TcpListener tcpServer = null;
        UdpClient udpServer = null;

        // Global variables for server's IP address and ports to use for services
        IPAddress ipAddress = IPAddress.Loopback;
        int tcpPort = 11000;
        int udpPort = 11001;

        public MainInterface()
        {
            InitializeComponent();
            lstStatus.DrawMode = DrawMode.OwnerDrawVariable;
            lstStatus.MeasureItem += lst_MeasureItem;
            lstStatus.DrawItem += lst_DrawItem;

            // Turn on the interfaces on launch
            TCPSwitch();
            UDPSwitch();
        }

        private void TCPSwitch()
        {
            // If TCP service is running, shut down cleanly; If not, launch a new threaded instance.
            if (tcpServerStatus)
            {
                tcpServer.Stop();
                tcpThread.Abort();
                tcpServerStatus = false;
                btnTCPSwitch.Text = "Start TCP Server";
            }
            else
            {
                tcpThread = new Thread(StartTCPListener);
                tcpThread.IsBackground = true;
                tcpThread.Start();
                tcpServerStatus = true;
                btnTCPSwitch.Text = "Stop TCP Server";
            }
        }

        private void UDPSwitch()
        {
            // If UDP service is running, shut down cleanly; If not, launch a new threaded instance.
            if (udpServerStatus)
            {
                udpServer.Close();
                udpThread.Abort();
                udpServerStatus = false;
                btnUDPSwitch.Text = "Start UDP Server";
            }
            else
            {
                udpThread = new Thread(StartUDPListener);
                udpThread.IsBackground = true;
                udpThread.Start();
                udpServerStatus = true;
                btnUDPSwitch.Text = "Stop UDP Server";
            }
        }

        private void RestartServers()
        {
            // Cleanly shuts down both TCP and UDP services and launches new threaded instances after a short delay.
            AddStatus("[!] Restarting TCP and UDP Servers");

            if (tcpServerStatus)
            {
                tcpServer.Stop();
                tcpThread.Abort();
                tcpServerStatus = false;
            }
            if (udpServerStatus)
            {
                udpServer.Close();
                udpThread.Abort();
                udpServerStatus = false;
            }

            Thread.Sleep(200);

            tcpThread = new Thread(StartTCPListener);
            tcpThread.IsBackground = true;
            tcpThread.Start();
            tcpServerStatus = true;

            udpThread = new Thread(StartUDPListener);
            udpThread.IsBackground = true;
            udpThread.Start();
            udpServerStatus = true;
        }

        private void AddStatus(string StatusMessage)
        {
            // Provides thread-safe status message updates to the interface and ensures that the latest message is shown.
            try
            {
                if (lstStatus.InvokeRequired)
                {
                    Invoke((MethodInvoker)delegate
                    {
                        lstStatus.Items.Add(StatusMessage);
                        lstStatus.TopIndex = lstStatus.Items.Count - 1;
                        lstStatus.Refresh();
                    });
                }
                else
                {
                    lstStatus.Items.Add(StatusMessage);
                    lstStatus.TopIndex = lstStatus.Items.Count - 1;
                    lstStatus.Refresh();
                }
            }
            catch (Exception)
            { }
        }

        // Following 2 methods are used for ensuring text on interface will not spill over horizontally (Author: Kosmo零, https://stackoverflow.com/questions/17613613/winforms-dotnet-listbox-items-to-word-wrap-if-content-string-width-is-bigger-tha)
        private void lst_MeasureItem(object sender, MeasureItemEventArgs e)
        {
            e.ItemHeight = (int)e.Graphics.MeasureString(lstStatus.Items[e.Index].ToString(), lstStatus.Font, lstStatus.Width).Height;
        }

        private void lst_DrawItem(object sender, DrawItemEventArgs e)
        {
            e.DrawBackground();
            e.DrawFocusRectangle();
            e.Graphics.DrawString(lstStatus.Items[e.Index].ToString(), e.Font, new SolidBrush(e.ForeColor), e.Bounds);
        }

        //Main TCP Listener instance for handling TCP connections with client applications
        private void StartTCPListener()
        {
            tcpServer = new TcpListener(ipAddress, tcpPort);

            char[] delimiters = new char[] { ' ', '\r', '\n' };

            // Bind the socket to the local endpoint and listen for incoming connections.  
            try
            {
                tcpServer.Start();
                AddStatus($"[TCP] Server Online! {ipAddress} waiting for connection on TCP port {tcpPort}...\r\n---");

                while (true)
                {
                    TcpClient client = tcpServer.AcceptTcpClient();
                    AddStatus($"[TCP] Server connected to {client.Client.RemoteEndPoint.ToString()}");

                    // Buffer for receiving data
                    Byte[] bytes = new Byte[512];

                    // Get a stream object for reading and writing
                    NetworkStream stream = client.GetStream();

                    int i;

                    // Loop to receive all the data sent by the client.
                    while ((i = stream.Read(bytes, 0, bytes.Length)) != 0)
                    {
                        string tcpReceived = Encoding.ASCII.GetString(bytes, 0, i);
                        
                        // Translate data bytes to a ASCII string.
                        AddStatus($"[TCP] Packet received from {client.Client.RemoteEndPoint.ToString()} : {tcpReceived}");

                        // Process the data sent by the client.
                        int wordCount = tcpReceived.Split(delimiters, StringSplitOptions.RemoveEmptyEntries).Length;
                        AddStatus($"[TCP] Counting words...: {wordCount}");
                        byte[] tcpResponse = Encoding.ASCII.GetBytes(wordCount.ToString());

                        // Send back a response.
                        stream.Write(tcpResponse, 0, tcpResponse.Length);
                        AddStatus($"[TCP] Packet data sent to {client.Client.RemoteEndPoint.ToString()} : {Encoding.ASCII.GetString(tcpResponse, 0, tcpResponse.Length)}");
                    }

                    // Shutdown and end connection
                    AddStatus($"[TCP] Closing connection with {client.Client.RemoteEndPoint.ToString()}");
                    client.Close();
                    AddStatus("---");
                }
            }
            catch (SocketException e)
            {
                // Catch any socket exceptions and display on interface for debugging.
                AddStatus($"[TCP] Error!: {e.ToString()}\r\n---");
            }
            finally
            {
                // The graceful stahp.
                tcpServer.Stop();
                AddStatus("[TCP] Server Offline.\r\n---");
            }

        }

        private void StartUDPListener()
        {
            // Bind the UDP port and accept communications from any IP
            udpServer = new UdpClient(udpPort);
            IPEndPoint groupEP = new IPEndPoint(IPAddress.Any, udpPort);

            DataTable calc = new DataTable();

            try
            {
                AddStatus($"[UDP] Server Online! {ipAddress} waiting for connection on UDP port {udpPort}...\r\n---");

                // Infinite loop to receive any datagrams sent by clients and provide responses.
                while (true)
                {
                    byte[] udpComms = udpServer.Receive(ref groupEP);

                    // Translate data bytes to a ASCII string.
                    string udpReceived = Encoding.ASCII.GetString(udpComms, 0, udpComms.Length);

                    AddStatus($"[UDP] Datagram received from {groupEP} : {udpReceived}");

                    // Process the data sent by the client (provide simple error checking on input).
                    string answer = $"{udpReceived} = ";
                    try
                    {
                        answer += calc.Compute(udpReceived, "").ToString();
                        AddStatus($"[UDP] Performing quick-math...: {udpReceived} = {answer}");
                    }
                    catch
                    {
                        answer = "Invalid input - try again.";
                        AddStatus($"[UDP] Invalid input received: {udpReceived}");
                    }

                    // Send back a response.
                    byte[] udpResponse = Encoding.ASCII.GetBytes(answer);
                    udpServer.Send(udpResponse, udpResponse.Length, groupEP);
                    AddStatus($"[UDP] Datagram sent to {groupEP} : {Encoding.ASCII.GetString(udpResponse, 0, udpResponse.Length)}");
                    AddStatus("---");
                }
            }
            catch (SocketException e)
            {
                // Catch any socket exceptions and display on interface for debugging.
                AddStatus($"[UDP] Error!: {e.ToString()}\r\n---");
            }
            finally
            {
                // The graceful stahp.
                udpServer.Close();
                AddStatus("[UDP] Server Offline.\r\n---");
            }

        }

        private void btnTCPSwitch_Click(object sender, EventArgs e)
        {
            TCPSwitch();
        }

        private void btnUDPSwitch_Click(object sender, EventArgs e)
        {
            UDPSwitch();
        }

        private void btnRestart_Click(object sender, EventArgs e)
        {
            RestartServers();
        }
    }
}

