
namespace CMP109SocketServer
{
    partial class MainInterface
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            System.ComponentModel.ComponentResourceManager resources = new System.ComponentModel.ComponentResourceManager(typeof(MainInterface));
            this.lstStatus = new System.Windows.Forms.ListBox();
            this.btnTCPSwitch = new System.Windows.Forms.Button();
            this.btnUDPSwitch = new System.Windows.Forms.Button();
            this.lblStatus = new System.Windows.Forms.Label();
            this.btnRestart = new System.Windows.Forms.Button();
            this.SuspendLayout();
            // 
            // lstStatus
            // 
            this.lstStatus.FormattingEnabled = true;
            this.lstStatus.HorizontalScrollbar = true;
            this.lstStatus.ItemHeight = 15;
            this.lstStatus.Items.AddRange(new object[] {
            "Assessment Test Server Status Messages:",
            "---"});
            this.lstStatus.Location = new System.Drawing.Point(10, 27);
            this.lstStatus.Name = "lstStatus";
            this.lstStatus.Size = new System.Drawing.Size(730, 379);
            this.lstStatus.TabIndex = 0;
            // 
            // btnTCPSwitch
            // 
            this.btnTCPSwitch.Location = new System.Drawing.Point(11, 412);
            this.btnTCPSwitch.Name = "btnTCPSwitch";
            this.btnTCPSwitch.Size = new System.Drawing.Size(152, 37);
            this.btnTCPSwitch.TabIndex = 1;
            this.btnTCPSwitch.Text = "Start TCP Server";
            this.btnTCPSwitch.UseVisualStyleBackColor = true;
            this.btnTCPSwitch.Click += new System.EventHandler(this.btnTCPSwitch_Click);
            // 
            // btnUDPSwitch
            // 
            this.btnUDPSwitch.Location = new System.Drawing.Point(169, 412);
            this.btnUDPSwitch.Name = "btnUDPSwitch";
            this.btnUDPSwitch.Size = new System.Drawing.Size(152, 37);
            this.btnUDPSwitch.TabIndex = 2;
            this.btnUDPSwitch.Text = "Start UDP Server";
            this.btnUDPSwitch.UseVisualStyleBackColor = true;
            this.btnUDPSwitch.Click += new System.EventHandler(this.btnUDPSwitch_Click);
            // 
            // lblStatus
            // 
            this.lblStatus.AutoSize = true;
            this.lblStatus.Location = new System.Drawing.Point(8, 8);
            this.lblStatus.Name = "lblStatus";
            this.lblStatus.Size = new System.Drawing.Size(136, 17);
            this.lblStatus.TabIndex = 5;
            this.lblStatus.Text = "Status messages:";
            // 
            // btnRestart
            // 
            this.btnRestart.Location = new System.Drawing.Point(588, 412);
            this.btnRestart.Name = "btnRestart";
            this.btnRestart.Size = new System.Drawing.Size(152, 37);
            this.btnRestart.TabIndex = 6;
            this.btnRestart.Text = "Restart Servers";
            this.btnRestart.UseVisualStyleBackColor = true;
            this.btnRestart.Click += new System.EventHandler(this.btnRestart_Click);
            // 
            // MainInterface
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(7F, 15F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(751, 456);
            this.Controls.Add(this.btnRestart);
            this.Controls.Add(this.lblStatus);
            this.Controls.Add(this.btnUDPSwitch);
            this.Controls.Add(this.btnTCPSwitch);
            this.Controls.Add(this.lstStatus);
            this.Font = new System.Drawing.Font("Consolas", 7.8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.FormBorderStyle = System.Windows.Forms.FormBorderStyle.FixedSingle;
            this.Icon = ((System.Drawing.Icon)(resources.GetObject("$this.Icon")));
            this.Name = "MainInterface";
            this.Text = "CMP109 Assessment Test Server";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.ListBox lstStatus;
        private System.Windows.Forms.Button btnTCPSwitch;
        private System.Windows.Forms.Button btnUDPSwitch;
        private System.Windows.Forms.Label lblStatus;
        private System.Windows.Forms.Button btnRestart;
    }
}

